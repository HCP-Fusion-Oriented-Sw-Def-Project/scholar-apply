package com.example.gbdpuserac.mapper;

import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpuserac.model.UacRoleMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色菜单中间表 Mapper 接口
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Mapper
public interface UacRoleMenuMapper extends IMapper<UacRoleMenu> {

    int deleteRoleMenuByMenuIds(String[] ids);

    int deleteRoleMenuByRoleIds(String[] ids);
}
