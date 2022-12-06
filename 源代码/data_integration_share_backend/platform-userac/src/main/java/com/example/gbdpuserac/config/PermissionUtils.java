package com.example.gbdpuserac.config;

import com.example.gbdpbootcore.annotation.DataScopePermission;
import com.example.gbdpuserac.core.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.reflection.MetaObject;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义权限相关工具类
 *
 * @author kongweichang
 */
@Slf4j
public class PermissionUtils {


    /**
     * 需要校验数据权限的mapper方法名
     */
    private static final List<String> DATA_SCOPE_METHOD_NAMES = Arrays.asList(
            "selectAll", "selectCount", "select", "select_COUNT",
            "listPage", "listPage_COUNT",
            "findUser", "findUser_COUNT"
    );

    /**
     * 判断当前请求mapper方法是否需要校验数据权限
     *
     * @param statementHandler
     * @return
     */
    public static Boolean isDataScopePermissionMethod(MetaObject statementHandler) {
        try {
            MappedStatement mappedStatement = (MappedStatement) statementHandler.getValue("delegate.mappedStatement");
            String id = mappedStatement.getId();
            String className = id.substring(0, id.lastIndexOf("."));
            String methodName = id.substring(id.lastIndexOf(".") + 1, id.length());

            // 首先校验@DataScopePermission注解
            final Class cls = Class.forName(className);
            final Method[] method = cls.getMethods();
            for (Method me : method) {
                if (me.getName().equals(methodName) && me.isAnnotationPresent(DataScopePermission.class)) {
                    DataScopePermission annotation = me.getAnnotation(DataScopePermission.class);
                    return annotation.filterSwitchOpen();
                }
            }
            // 检查参数有没有sqlmap参数
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
                if (StringUtils.isNotEmpty(dsSql)) {
                    return true;
                }
            }
            // 校验请求的方法是否是默认需要校验数据权限的方法
            if (DATA_SCOPE_METHOD_NAMES.contains(methodName)) {
                return true;
            }
        } catch (Exception e) {
            log.warn("[PermissionUtils-isDataScopePermissionMethod]::error::", e);
        }
        return false;
    }
}
