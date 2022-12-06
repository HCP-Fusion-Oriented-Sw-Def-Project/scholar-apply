package com.example.gbdpuserac.mapper;


import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpuserac.model.UacRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Mapper
public interface UacRoleMapper extends IMapper<UacRole> {
    List<UacRole> listRolesByUserId(String[] userId);
}
