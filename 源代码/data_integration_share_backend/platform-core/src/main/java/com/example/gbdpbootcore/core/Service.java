package com.example.gbdpbootcore.core;

import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import org.apache.ibatis.exceptions.TooManyResultsException;
import tk.mybatis.mapper.entity.Condition;

import java.util.List;

/**
 * Service 层 基础接口，其他Service 接口 请继承该接口
 */
public interface Service<T> {
    /**
     * 持久化
     *
     * @param model
     */
    int save(T model);

    /**
     * 批量持久化
     *
     * @param models
     */
    int saveBatch(List<T> models);

    /**
     * 通过主鍵刪除
     * @param id
     */
    int deleteById(String id);

    /**
     * 批量刪除 eg：ids -> “1,2,3,4”
     * @param ids
     */
    int deleteByIds(String ids);

    /**
     * 更新
     * @param model
     */
    int update(T model);

    /**
     * 根据ID 批量更新
     *
     * @param entityList 实体对象集合
     */
    int updateBatch(List<T> entityList);

    /**
     * 通过ID查找
     * @param id
     * @return
     */
    T getById(String id);

    /**
     * 通过Model中某个成员变量名称（非数据表中column的名称）查找,value需符合unique约束
     *
     * @return
     * @throws TooManyResultsException
     */
    T getOne(T model) throws TooManyResultsException;

    /**
     * 根据条件查找所有
     *
     * @param condition
     * @return
     */
    @Deprecated
    List<T> listByCondition(Condition condition);

    /**
     * 将 model 保存或更新
     * @param model
     * @return
     */
    int saveOrUpdate(T model);

    /**
     * 根据 实体对象 条件，查询总记录数
     *
     * @param query 实体对象
     */
    int count(T query);

    /**
     * 查询列表
     *
     * @param query 实体对象
     */
    List<T> list(T query);

    /**
     * 翻页查询
     *
     * @param page         翻页对象
     * @param query       实体对象封装
     */
    PageResult<T> page(PageRequest page, T query);
}
