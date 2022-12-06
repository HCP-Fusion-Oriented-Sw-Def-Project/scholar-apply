package com.example.gbdpuserac.service;


import com.example.gbdpbootcore.core.Service;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpuserac.dto.UacRoleDto;
import com.example.gbdpuserac.model.UacRole;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
public interface UacRoleService extends Service<UacRole> {

    PageResult listRolesByUserId(PageRequest pageRequest, String userid);

    @Transactional(readOnly = false, rollbackFor = Exception.class)
    boolean saveRoleAndMenu(String roleId, List<String> menuIdList);

    UacRoleDto getRoleWithMenu(String id);

    int deleteRoleById(String id);

    int deleteRoleByIds(String idsStr);

    boolean checkDuplicateRole(UacRole uacRole);

    boolean saveRoleAndUser(String roleId, String... userId);

    boolean saveRoleAndOffice(String roleId, List<String> officeIdList);

    boolean checkRoleExist(String roleId);

    boolean deleteRoleAndUser(String roleId, String... userIds);

    boolean saveOrUpdateRole(UacRole uacRole);
}
