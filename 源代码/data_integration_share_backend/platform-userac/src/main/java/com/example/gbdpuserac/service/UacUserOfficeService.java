package com.example.gbdpuserac.service;


import com.example.gbdpbootcore.core.Service;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.model.UacUserOffice;

import java.util.List;

/**
 * <p>
 * 用户机构中间表 服务类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
public interface UacUserOfficeService extends Service<UacUserOffice> {

    boolean saveList(String id, List<String> officeIdList);

    boolean updateList(List<UacUserOffice> deletedUserOfficeList);

    List<UacUserDto> listUserByOfficeId(String officeId);

    boolean updateUserByOfficeId(String officeId, List<String> userIds);
}
