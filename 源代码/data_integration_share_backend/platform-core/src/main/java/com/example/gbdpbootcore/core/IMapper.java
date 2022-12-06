package com.example.gbdpbootcore.core;

import com.example.gbdpbootcore.annotation.DataScopePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ConditionMapper;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

/**
 * 定制版MyBatis Mapper插件接口，如需其他接口参考官方文档自行添加。
 */
public interface IMapper<T>
        extends
        BaseMapper<T>,
        ConditionMapper<T>,
        IdsMapper<T>,
        InsertListMapper<T> {

    /**
     * 单条逻辑删除
     *
     * @param id id
     * @return int
     * @author kwc
     * @date 2019/12/14 16:37
     */
    int deleteByLogic(String id);

    /**
     * 批量逻辑删除
     *
     * @param ids id Str
     * @return int
     * @author kwc
     * @date 2019/12/14 16:37
     */
    int deleteManyByLogic(String[] ids);

    @DataScopePermission
    List<T> listPage(T record);

    T get(@Param("id") String id);
}
