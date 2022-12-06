package com.example.gbdpuserac.mapper;

import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpuserac.model.UacUser;
import com.example.gbdpuserac.model.UacUserOffice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 用户机构中间表 Mapper 接口
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Mapper
public interface UacUserOfficeMapper extends IMapper<UacUserOffice> {

    List<UacUser> listUserByOfficeId(String officeId);

    int deleteByOfficeIds(String[] ids);

    int deleteUserOfficeByUserIds(String[] ids);
}
