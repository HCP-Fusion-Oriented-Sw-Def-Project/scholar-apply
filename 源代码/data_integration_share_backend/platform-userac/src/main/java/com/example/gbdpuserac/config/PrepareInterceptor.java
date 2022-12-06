package com.example.gbdpuserac.config;

import com.example.gbdpbootcore.enums.RoleDataScopeEnum;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpuserac.core.BaseEntity;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.security.UacUserUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * mybatis数据权限拦截器 - prepare
 *
 * @author kongweichang
 */

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
@Slf4j
@Data
public class PrepareInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        StatementHandler handler = (StatementHandler) invocation.getTarget();
        //由于mappedStatement为protected的，所以要通过反射获取
        //获取sql
        BoundSql boundSql = handler.getBoundSql();
        String sql = boundSql.getSql();

        if (log.isInfoEnabled()) {
            log.info("数据权限处理【SQL:[{}]】", sql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 数据范围过滤
     *
     * @param statementHandler
     * @param user             当前用户对象
     * @param oldSql           默认sql
     * @param isPermitAllUrl
     * @return 标准连接条件对象
     */
    public String getDataScopeSql(MetaObject statementHandler, UacUserDto user, String oldSql, boolean isPermitAllUrl) {

        StringBuilder newSql = new StringBuilder(oldSql);

        boolean isCountSql = newSql.toString().toLowerCase().contains("count(");
        boolean hasWhere = newSql.toString().toUpperCase().contains("WHERE");

        // 如果是不需要校验数据权限，直接请求所有数据
        if (isPermitAllUrl) {
            if (isCountSql) {
                if (hasWhere) {
                    newSql = new StringBuilder(newSql.append(" AND del_flag = 0 "));
                } else {
                    newSql = new StringBuilder(newSql.append(" WHERE del_flag = 0 "));
                }
            } else {
                newSql = new StringBuilder("select * from (").append(newSql).append(" ) s where s.del_flag = 0 ");
            }
            return newSql.toString();
        }

        // 第一版数据权限过滤
        //return getDataScopeSqlByStringBuilder(user, oldSql, isPermitAllUrl);
        // 第二版数据权限过滤
        //return getDataScopeSqlByJSqlParser(oldSql, user);
        // 仿照jee实现权限过滤
        //return getDataScopeSqlBySqlMap(statementHandler, user, oldSql, hasWhere);
        return oldSql;
    }

    /**
     * 仿照jee实现
     * jee通过mapper中获取sqlMap参数，本方法通过拦截器获取
     *
     * @param statementHandler
     * @param user
     * @param oldSql
     * @param hasWhere
     * @return
     */
    private String getDataScopeSqlBySqlMap(MetaObject statementHandler, UacUserDto user, String oldSql, boolean hasWhere) {
        StringBuilder newSql = new StringBuilder(oldSql);
        ParameterHandler parameterHandler = (ParameterHandler) statementHandler.getValue("delegate.parameterHandler");
        Object parameterObject = parameterHandler.getParameterObject();
        // 代码新增sql语句
        StringBuilder dsSql = new StringBuilder();

        if (parameterObject != null) {
            if (parameterObject instanceof List) {
                List<Object> parameterList = (List) parameterObject;
                for (Object parameter : parameterList) {
                    if (parameter instanceof BaseEntity) {
                        BaseEntity param = (BaseEntity) parameter;
                        Map<String, String> sqlMap = param.getSqlMap();
                        dsSql.append(sqlMap.getOrDefault("dsSql", ""));
                    }
                }
            } else if (parameterObject instanceof Map) {
                Map<Object, Object> parameterMap = (Map) parameterObject;
                for (Map.Entry<Object, Object> parameter : parameterMap.entrySet()) {
                    Object key = parameter.getKey();
                    Object value = parameter.getValue();
                    if ("sqlMap".equals(key)) {
                        HashMap<String, String> sqlMap = (HashMap<String, String>) value;
                        dsSql.append(sqlMap.getOrDefault("dsSql", ""));
                    }
                    if (value instanceof BaseEntity) {
                        BaseEntity param = (BaseEntity) value;
                        Map<String, String> sqlMap = param.getSqlMap();
                        dsSql.append(sqlMap.getOrDefault("dsSql", ""));
                    }
                }
            } else if (parameterObject instanceof BaseEntity) {
                BaseEntity param = (BaseEntity) parameterObject;
                Map<String, String> sqlMap = param.getSqlMap();
                dsSql = new StringBuilder(sqlMap.getOrDefault("dsSql", ""));
            }
        }
        if (StringUtils.isNotEmpty(dsSql.toString())) {
            return hasWhere ? newSql.append(" AND ").append(dsSql).toString() :
                    newSql.append(" WHERE ").append(dsSql).toString();
        } else {
            return newSql.toString();
        }
    }

    /**
     * 使用 CCJSqlParserUtil 处理sql
     * 权限sql包装
     * 拼接user-office数据库作为数据权限查询
     * 思路 把一条sql按照split分成 select from join where order 几部分 然后 进行各部分拼接
     *
     * @param oldSql
     * @param user
     * @return
     */
    private String getDataScopeSqlByJSqlParser(String oldSql, UacUserDto user) {

        // 进行权限过滤，多个角色权限范围之间为或者关系。
        String maxDataScope = UacUserUtils.getMaxDataScope(user);
        String uid = user.getId();
        String companyId = UacUserUtils.getCompanyId(user);
        RoleDataScopeEnum dataScopeEnum = RoleDataScopeEnum.getDataScope(maxDataScope);
        List<String> officeIds = UacUserUtils.getOfficeId(user);

        Select select = null;
        String tableAlias = "t";
        try {
            select = (Select) CCJSqlParserUtil.parse(oldSql);

        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
        PlainSelect plainSelect = ((PlainSelect) select.getSelectBody());
        Alias alias = plainSelect.getFromItem().getAlias();
        if (alias != null) {
            tableAlias = alias.getName();
        }
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        String selectItemStr = selectItems.toString();
        FromItem fromItem = plainSelect.getFromItem();
        //模板sql，
        String modelSql;

        switch (dataScopeEnum) {
            case DATA_SCOPE_ALL:
                modelSql = "select * from tableName " + tableAlias + " where " + tableAlias + ".del_flag = 0";
                break;
            case DATA_SCOPE_COMPANY:
                modelSql = "select * from tableName " + tableAlias +
                        " LEFT JOIN uac_user dso_uu ON dso_uu.id = " + tableAlias + ".create_by " +
                        " LEFT JOIN uac_user_office dso_uuo ON dso_uuo.user_id = dso_uu.id " +
                        " LEFT JOIN uac_office dso_uo ON dso_uo.id = dso_uuo.office_id " +
                        " where office_id like concat(\"" + companyId + "\",'%') AND " + tableAlias + ".del_flag = 0";
                break;
            case DATA_SCOPE_OFFICE:
                // in
                String officeIdsStr = parseOfficeIdListString(officeIds);
                modelSql = "select * from tableName " + tableAlias +
                        " LEFT JOIN uac_user dso_uu ON dso_uu.id = " + tableAlias + ".create_by " +
                        " LEFT JOIN uac_user_office dso_uuo ON dso_uuo.user_id = dso_uu.id " +
                        " LEFT JOIN uac_office dso_uo ON dso_uo.id = dso_uuo.office_id " +
                        " WHERE office_id IN " + officeIdsStr + " AND " + tableAlias + ".del_flag = 0";
                break;
            case DATA_SCOPE_SELF:
            case DATA_SCOPE_CUSTOM:
                String userParam = "(" + tableAlias + ".create_by = \"" + uid + "\"";
                if (selectItemStr.contains("user_id")) {
                    userParam = userParam + " or user_id = \"" + uid + "\"";
                } else if (selectItemStr.contains("uid")) {
                    userParam = userParam + " or uid = \"" + uid + "\"";
                } else if (fromItem.toString().contains("uac_user")) {
                    userParam = userParam + " or " + tableAlias + ".id = \"" + uid + "\"";
                }
                userParam = userParam + ")";
                modelSql = "select * from tableName " + tableAlias +
                        " LEFT JOIN uac_user dso_uu ON dso_uu.id = " + tableAlias + ".create_by " +
                        " LEFT JOIN uac_user_office dso_uuo ON dso_uuo.user_id = dso_uu.id " +
                        " LEFT JOIN uac_office dso_uo ON dso_uo.id = dso_uuo.office_id " +
                        " where " + userParam + " AND " + tableAlias + ".del_flag = 0";
                break;
            default:
                modelSql = "select * from tableName " + tableAlias +
                        " LEFT JOIN uac_user dso_uu ON dso_uu.id = " + tableAlias + ".create_by " +
                        " LEFT JOIN uac_user_office dso_uuo ON dso_uuo.user_id = dso_uu.id " +
                        " LEFT JOIN uac_office dso_uo ON dso_uo.id = dso_uuo.office_id " +
                        " where " + tableAlias + ".del_flag = 0";
                break;
        }
        // 拼接modelSql和oldSql
        try {
            Select modelSelect = (Select) CCJSqlParserUtil.parse(modelSql);
            PlainSelect modelPlainSelect = ((PlainSelect) modelSelect.getSelectBody());

            // where处理
            Expression where = plainSelect.getWhere();
            Expression modelWhere = modelPlainSelect.getWhere();
            String whereStr = where != null ? where.toString() : "";
            String modelWhereStr = modelWhere != null ? modelWhere.toString() : "";
            Expression newWhere;
            if (StringUtils.isEmpty(whereStr)) {
                newWhere = CCJSqlParserUtil.parseCondExpression(modelWhereStr);
            } else if (StringUtils.isEmpty(modelWhereStr)) {
                newWhere = CCJSqlParserUtil.parseCondExpression(whereStr);
            } else {
                newWhere = CCJSqlParserUtil.parseCondExpression(whereStr + " AND " + modelWhereStr);
            }

            // join处理
            List<Join> joins = plainSelect.getJoins() == null ?
                    new ArrayList<>() : plainSelect.getJoins();
            List<Join> modelJoins = modelPlainSelect.getJoins() == null ?
                    new ArrayList<>() : modelPlainSelect.getJoins();
            // join暂时不需要去重
            joins.addAll(modelJoins);
            // from 处理
            List<SelectItem> items = plainSelect.getSelectItems();
            if (alias == null) {
                // 加t需要处理select item
                alias = new Alias("t");
                Alias finalAlias = alias;
                List<SelectExpressionItem> collect = items.stream().map(
                        selectItem -> {
                            SelectExpressionItem expressionItem = (SelectExpressionItem) selectItem;
                            Expression expression = expressionItem.getExpression();
                            if (expression instanceof Column) {
                                Column column = (Column) expression;
                                Table table = column.getTable();
                                if (table == null) {
                                    column.setTable(new Table(finalAlias.getName()));
                                }
                                expressionItem.setExpression(expression);
                            }
                            return expressionItem;
                        }
                ).collect(Collectors.toList());
                plainSelect.setSelectItems(Collections.unmodifiableList(collect));
            }
            fromItem.setAlias(alias);
            plainSelect.setFromItem(fromItem);
            plainSelect.setJoins(joins);
            plainSelect.setWhere(newWhere);
            return plainSelect.toString();
        } catch (JSQLParserException e) {
            e.printStackTrace();
            log.error("[genDataScopeSql-JSQLParserException-ERROR]:[oldSql={}]", oldSql, e);
            return oldSql;
        }
    }

    private String parseOfficeIdListString(List<String> officeIds) {
        if (CollectionUtils.isEmpty(officeIds)) {
            return "()";
        }
        StringBuffer s = new StringBuffer();
        s.append("(");
        officeIds.forEach(a -> s.append("\"").append(a).append("\"").append(","));
        s.delete(s.length() - 1, s.length()).append(")");
        return s.toString();
    }

    private String getDataScopeSqlByStringBuilder(UacUserDto user, String oldSql, boolean isPermitAllUrl) {
        StringBuilder newSql = new StringBuilder(oldSql);
        if (GyToolUtil.isNull(user) && !isPermitAllUrl) {
            // TODO: 2019/12/15 0015 游客数据权限设计
            //newSql = new StringBuilder("select * from (").append(newSql).append(" ) s where s.del_flag = 0 limit 1");
            newSql = new StringBuilder("select * from (").append(newSql).append(" ) s where s.del_flag = 0 limit 1;");
            return newSql.toString();
        }
        boolean isCountSql = newSql.toString().toLowerCase().contains("count(");
        boolean hasWhere = newSql.toString().toUpperCase().contains("WHERE");
        // 进行权限过滤，多个角色权限范围之间为或者关系。
        String maxDataScope = UacUserUtils.getMaxDataScope(user);
        String userId = user.getId();
        String officeIds = String.valueOf(UacUserUtils.getOfficeId(user));
        String companyId = UacUserUtils.getCompanyId(user);

        if (RoleDataScopeEnum.DATA_SCOPE_ALL == RoleDataScopeEnum.getDataScope(maxDataScope)) {
            if (isCountSql) {
                if (hasWhere) {
                    newSql = new StringBuilder(newSql.append(" AND del_flag = 0 "));
                } else {
                    newSql = new StringBuilder(newSql.append(" WHERE del_flag = 0 "));
                }
            } else {
                newSql = new StringBuilder("select * from (").append(newSql).append(" ) s where s.del_flag = 0 ");
            }
        } else if (RoleDataScopeEnum.DATA_SCOPE_COMPANY == RoleDataScopeEnum.getDataScope(maxDataScope)) {
            if (isCountSql) {
                if (hasWhere) {
                    newSql = new StringBuilder(newSql.append(" AND del_flag = 0 "));
                } else {
                    newSql = new StringBuilder(newSql.append(" WHERE del_flag = 0 "));
                }
                if (oldSql.contains("office_id")) {
                    newSql.append(" and office_id like concat(\"").append(companyId).append("\",'%')");
                }
            } else {
                newSql = new StringBuilder("select * from (").append(newSql).append(" ) s where s.del_flag = 0 ");
                if (oldSql.contains("office_id")) {
                    newSql.append(" and s.office_id like concat(\"").append(companyId).append("\",'%')");
                }
                // 连表查询office和user，限制dataScope
                /*newSql = new StringBuilder("select * from (").append(newSql).append(" ) dso ");
                newSql.append("JOIN uac_user uu ON uu.id = dso.createBy " +
                        " JOIN uac_user_office uuo ON uuo.user_id = uu.id " +
                        " JOIN uac_office uo ON uo.id = uuo.office_id ");*/
            }
            newSql.append(";");
        } else if (RoleDataScopeEnum.DATA_SCOPE_OFFICE == RoleDataScopeEnum.getDataScope(maxDataScope)) {
            if (isCountSql) {
                if (hasWhere) {
                    newSql = new StringBuilder(newSql.append(" AND del_flag = 0 "));
                } else {
                    newSql = new StringBuilder(newSql.append(" WHERE del_flag = 0 "));
                }
                if (oldSql.contains("office_id")) {
                    newSql.append(" and \"").append(officeIds).append("\" like concat('%', concat(office_id,'%'))");
                }
            } else {
                newSql = new StringBuilder("select * from (").append(newSql).append(" ) s where s.del_flag = 0 ");
                if (oldSql.contains("office_id")) {
                    newSql.append(" and \"").append(officeIds).append("\" like concat('%', concat(s.office_id,'%'))");
                }
            }
            newSql.append(";");
        } else if (RoleDataScopeEnum.DATA_SCOPE_SELF == RoleDataScopeEnum.getDataScope(maxDataScope)) {
            if (isCountSql) {
                if (hasWhere) {
                    newSql = new StringBuilder(newSql.append(" AND del_flag = 0 "));
                } else {
                    newSql = new StringBuilder(newSql.append(" WHERE del_flag = 0 "));
                }
                if (oldSql.contains("user_id")) {
                    newSql.append("and user_id = \"").append(userId).append("\"");
                }
                if (oldSql.contains("create_by")) {
                    newSql.append("and create_by = \"").append(userId).append("\"");
                }
                if (oldSql.contains("office_id")) {
                    newSql.append(" and \"").append(officeIds).append("\" like concat('%', concat(office_id,'%'))");
                }
            } else {
                newSql = new StringBuilder("select * from (").append(newSql).append(" ) s where s.del_flag = 0 ");
                if (oldSql.contains("user_id")) {
                    newSql.append("and s.user_id = \"").append(userId).append("\"");
                }
                if (oldSql.contains("office_id")) {
                    newSql.append(" and \"").append(officeIds).append("\" like concat('%', concat(s.office_id,'%'))");
                }
            }
            newSql.append(";");
        } else if (RoleDataScopeEnum.DATA_SCOPE_CUSTOM == RoleDataScopeEnum.getDataScope(maxDataScope)) {
            // TODO: 2019/12/15 0015 游客数据权限设计
            if (isCountSql) {
                newSql = new StringBuilder("SELECT COUNT(1);");
            } else {
                newSql = new StringBuilder("select * from (").append(newSql).append(" ) s where s.del_flag = 0 limit 1");
            }
        }
        return newSql.toString();
    }
}