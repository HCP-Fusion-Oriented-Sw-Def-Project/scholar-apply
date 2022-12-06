package com.example.gbdpuserac.dao;

import com.example.gbdpuserac.entity.RequestEventDept;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * (RequestEventDept)表数据库访问层
 *
 * @since 2021-09-06 20:35:51
 */
public interface RequestEventDeptDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    RequestEventDept queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param offset 查询起始位置
     * @param limit 查询条数
     * @return 对象列表
     */
    List<RequestEventDept> queryAllByLimit(@Param("offset") int offset, @Param("limit") int limit);


    /**
     * 通过实体作为筛选条件查询
     *
     * @param requestEventDept 实例对象
     * @return 对象列表
     */
    List<RequestEventDept> queryAll(RequestEventDept requestEventDept);

    /**
     * 新增数据
     *
     * @param requestEventDept 实例对象
     * @return 影响行数
     */
    int insert(RequestEventDept requestEventDept);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<RequestEventDept> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<RequestEventDept> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<RequestEventDept> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<RequestEventDept> entities);

    /**
     * 修改数据
     *
     * @param requestEventDept 实例对象
     * @return 影响行数
     */
    int update(RequestEventDept requestEventDept);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(String id);

}

