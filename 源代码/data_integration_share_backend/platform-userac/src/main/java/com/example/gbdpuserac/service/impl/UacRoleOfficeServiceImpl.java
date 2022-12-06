package com.example.gbdpuserac.service.impl;


import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.mapper.UacRoleOfficeMapper;
import com.example.gbdpuserac.model.UacRoleOffice;
import com.example.gbdpuserac.service.UacRoleOfficeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * <p>
 * 角色机构中间表 服务实现类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Service
@Transactional(readOnly = true)
public class UacRoleOfficeServiceImpl extends BaseService<UacRoleOffice> implements UacRoleOfficeService {

    @Resource
    private UacRoleOfficeMapper uacRoleOfficeMapper;

}
