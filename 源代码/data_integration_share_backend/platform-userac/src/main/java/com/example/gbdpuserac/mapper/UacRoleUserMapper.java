package com.example.gbdpuserac.mapper;

import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpuserac.model.UacRoleUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色用户中间表 Mapper 接口
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Mapper
public interface UacRoleUserMapper extends IMapper<UacRoleUser> {

    int deleteRoleUserByRoleIds(String[] ids);

    int deleteRoleUserByUserIds(String[] ids);
}
