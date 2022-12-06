package com.example.gbdpuserac.core;

import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpbootcore.core.Service;
import com.example.gbdpbootcore.enums.RoleDataScopeEnum;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.model.UacRole;
import com.example.gbdpuserac.security.UacUserUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 基于通用MyBatis Mapper插件的Service接口的实现
 * @author kongweichang
 */
public abstract class BaseService<T> implements Service<T> {

    protected Logger log = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected IMapper<T> mapper;
    /**
     * 获取当前User对象
     * @return UacUserDto
     */
    public static UacUserDto getUserInfo() {
        return UacUserUtils.getUserInfoFromRequest();
    }

    /**
     * 当前泛型真实类型的Class
     */
    private Class<T> modelClass;

    public BaseService() {
        ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
        modelClass = (Class<T>) pt.getActualTypeArguments()[0];
    }

    /**
     * 单个持久化
     *
     * @param model
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int save(T model) {
        if (model instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) model;
            baseEntity.preSave();
            model = (T) baseEntity;
        }
        return mapper.insertSelective(model);
    }

    /**
     * 批量持久化
     *
     * @param models
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int saveBatch(List<T> models) {
        if (CollectionUtils.isEmpty(models)) {
            return 0;
        }
        List<T> collect = models.stream().filter(Objects::nonNull).map(
                model ->
                {
                    if (model instanceof BaseEntity) {
                        BaseEntity baseEntity = (BaseEntity) model;
                        baseEntity.preSave();
                        return (T) baseEntity;
                    }
                    return model;
                }
        ).collect(Collectors.toList());
        for (T record : models) {
            mapper.insert(record);
        }
        return 1;
    }

    /**
     * 单个更新
     *
     * @param model
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int update(T model) {
        if (model instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) model;
            baseEntity.preUpdate();
            model = (T) baseEntity;
        }
        return mapper.updateByPrimaryKeySelective(model);
    }

    /**
     * 根据ID 批量更新
     *
     * @param models 实体对象集合
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int updateBatch(List<T> models) {
        List<T> collect = models.stream().filter(Objects::nonNull).map(
                model ->
                {
                    if (model instanceof BaseEntity) {
                        BaseEntity baseEntity = (BaseEntity) model;
                        baseEntity.preUpdate();
                        return (T) baseEntity;
                    }
                    return model;
                }
        ).collect(Collectors.toList());
        for (T t : collect) {
            mapper.updateByPrimaryKeySelective(t);
        }
        return 1;
    }

    /**
     * 根据id字段是否存在，选择更新/新增
     * @param model
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int saveOrUpdate(T model) {
        if (model instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) model;
            if (baseEntity.isNew()) {
                baseEntity.preSave();
                return mapper.insertSelective((T) baseEntity);
            }
            baseEntity.preUpdate();
            return mapper.updateByPrimaryKeySelective((T) baseEntity);
        }
        return 0;
    }

    /**
     * 逻辑删除单条
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteById(String id) {
        //默认逻辑删除
        return mapper.deleteByLogic(id);
    }

    /**
     * 逻辑删除多条
     *
     * @param ids
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteByIds(String ids) {
        //默认逻辑删除
        return mapper.deleteManyByLogic(ids.split(","));
    }

    @Override
    public T getOne(T model) throws TooManyResultsException {
        if (model instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) model;
            baseEntity.setDelFlag("0");
            model = (T) baseEntity;
        }
        return mapper.selectOne(model);
    }

    /**
     * 通过primary key 获取单条
     * @param id
     * @return
     */
    @Override
    public T getById(String id) {
        return mapper.get(id);
    }

    @Override
    public List<T> listByCondition(Condition condition) {
        if (condition != null) {
            condition.createCriteria().andEqualTo("del_flag", "0");
        }
        return mapper.selectByCondition(condition);
    }

    /**
     * 根据 实体对象 条件，查询总记录数
     *
     * @param query 实体对象
     */
    @Override
    public int count(T query) {
        if (query instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) query;
            baseEntity.setDelFlag("0");
            query = (T) baseEntity;
        }
        return mapper.selectCount(query);
    }

    /**
     * 查询列表
     *
     * @param query 实体对象
     */
    @Override
    public List<T> list(T query) {
        if (query instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) query;
            baseEntity.setDelFlag("0");
            query = (T) baseEntity;
        }
        return mapper.select(query);
    }

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param query       实体对象封装
     */
    @Override
    public PageResult<T> page(PageRequest page, T query) {
        String order = String.format("%s %s", page.getOrderBy(), page.getOrderRule());
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), order);
        List<T> select = mapper.listPage(query);
        return PageResult.getPageResult(new PageInfo<>(select));
    }

    /**
     * 数据范围过滤
     * @param user 以此用户的最大权限查询数据
     * @param officeAlias 机构表别名，多个用“,”逗号隔开。
     * @param userAlias 用户表别名，多个用“,”逗号隔开，传递空，忽略此参数
     * @return 标准连接条件对象
     */
    public static String dataScopeFilter(UacUserDto user, String officeAlias, String userAlias) {

        StringBuilder sqlString = new StringBuilder();
        UacRole maxDataScopeRole = UacUserUtils.getMaxDataScopeRole(user);
        // 进行权限过滤，多个角色权限范围之间为或者关系。
        List<UacRole> roleList = user.getRoleList();
        String userId = user.getId();
        List<String> officeIds = UacUserUtils.getOfficeId(user);
        boolean isDataScopeAll = false;
        String officeIdStr = parseOfficeIdListString(officeIds);

        for (UacRole role : roleList) {
            // 多级角色 sql拼接(机构过滤)
            for (String oa : StringUtils.split(officeAlias, ",")) {
                switch (RoleDataScopeEnum.getDataScope(Integer.valueOf(role.getDataScope()))) {
                    case DATA_SCOPE_ALL:
                        isDataScopeAll = true;
                        break;
                    case DATA_SCOPE_COMPANY:
                        sqlString.append(" OR " + oa + ".id IN " + officeIdStr + "");
                        for (String officeId : officeIds) {
                            if ("-1".equals(officeId)) {
                                continue;
                            }
                            sqlString.append(" OR ").append(oa).append(".parent_ids LIKE concat('%',\"").append(officeId).append("\",'%')");
                        }
                        break;
                    case DATA_SCOPE_OFFICE:
                        sqlString.append(" OR " + oa + ".id IN " + officeIdStr + "");
                        break;
                    case DATA_SCOPE_CUSTOM:
                        sqlString.append(" OR EXISTS (SELECT 1 FROM uac_role_office WHERE role_id = '" + maxDataScopeRole.getId() + "'");
                        sqlString.append(" AND office_id = " + oa +".id)");
                        break;
                    default:
                        break;
                }
            }
        }
        // 如果没有全部数据权限，并设置了用户别名，则当前权限为本人；如果未设置别名，当前无权限为已植入权限
        if (!isDataScopeAll && Integer.parseInt(maxDataScopeRole.getDataScope())
                <= RoleDataScopeEnum.DATA_SCOPE_SELF.getValue() ){
            if (StringUtils.isNotBlank(userAlias)){
                for (String ua : StringUtils.split(userAlias, ",")){
                    sqlString.append(" OR " + ua + ".id = '" + userId + "'");
                }
            }else {
                for (String oa : StringUtils.split(officeAlias, ",")){
                    sqlString.append(" OR " + oa + ".id IS NULL");
                }
            }
        }else{
            // 如果包含全部权限，则去掉之前添加的所有条件，并跳出循环。
            sqlString = new StringBuilder();
        }
        if (StringUtils.isNotBlank(sqlString.toString())){
            return " AND (" + sqlString.substring(4) + ")";
        }
        return "";
    }

    private static String parseOfficeIdListString(List<String> officeIds) {
        if (CollectionUtils.isEmpty(officeIds)) {
            return "()";
        }
        StringBuffer s = new StringBuffer();
        s.append("(");
        officeIds.forEach(a -> s.append("\"").append(a).append("\"").append(","));
        s.delete(s.length() - 1, s.length()).append(")");
        return s.toString();
    }
}
