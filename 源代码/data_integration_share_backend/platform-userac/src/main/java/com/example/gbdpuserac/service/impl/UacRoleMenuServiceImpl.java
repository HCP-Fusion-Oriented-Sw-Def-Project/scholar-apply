package com.example.gbdpuserac.service.impl;


import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.mapper.UacRoleMenuMapper;
import com.example.gbdpuserac.model.UacRoleMenu;
import com.example.gbdpuserac.service.UacRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * <p>
 * 角色菜单中间表 服务实现类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Service
@Transactional(readOnly = true)
public class UacRoleMenuServiceImpl extends BaseService<UacRoleMenu> implements UacRoleMenuService {

    @Resource
    private UacRoleMenuMapper uacRoleMenuMapper;

}
