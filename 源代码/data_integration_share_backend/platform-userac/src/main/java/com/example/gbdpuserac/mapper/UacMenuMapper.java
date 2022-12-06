package com.example.gbdpuserac.mapper;

import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpuserac.model.UacMenu;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Mapper
public interface UacMenuMapper extends IMapper<UacMenu> {

    /**
     * Find menu  listByPage by user id listByPage.
     *
     * @param userId the user id
     * @return the listByPage
     */
    List<UacMenu> listMenuByUserId(String userId);
    /**
     * Find menu permission listByPage by user id listByPage.
     *
     * @param userId the user id
     * @return the listByPage
     */
    List<String> listMenuPerByUserId(String userId);

    /**
     * 查询多个roleId的关联菜单的并集
     *
     * @param roleIds roleIds
     * @return List<UacMenu> 去重 sort排序
     */
    List<UacMenu> listMenuByRoleIds(List<String> roleIds);

    /**
     * 查询单个roleId关联的所有菜单
     * @param roleId roleId
     * @return List<UacMenu> sort排序
     */
    List<UacMenu> listMenuByRoleId(String roleId);
}
