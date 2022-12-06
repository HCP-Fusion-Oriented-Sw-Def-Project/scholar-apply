package com.example.gbdpuserac.mapper;

import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpuserac.model.UacRoleOffice;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 角色机构中间表 Mapper 接口
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Mapper
public interface UacRoleOfficeMapper extends IMapper<UacRoleOffice> {

    int deleteRoleOfficeByOfficeIds(String[] ids);

    int deleteRoleOfficeByRoleIds(String[] ids);
}
