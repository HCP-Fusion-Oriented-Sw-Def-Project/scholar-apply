package com.example.gbdpuserac.service.impl;

import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.model.UacRoleUser;
import com.example.gbdpuserac.service.UacRoleUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * <p>
 * 角色用户中间表 服务实现类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Service
@Transactional(readOnly = true)
public class UacRoleUserServiceImpl extends BaseService<UacRoleUser> implements UacRoleUserService {

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean saveList(String uid, List<String> roleIdList) {
        List<UacRoleUser> collect = roleIdList.stream().map(roleId -> {
            UacRoleUser uacRoleUser = new UacRoleUser();
            uacRoleUser.setRoleId(roleId);
            uacRoleUser.setUserId(uid);
            uacRoleUser.preSave();
            return uacRoleUser;
        }).collect(Collectors.toList());
        return saveBatch(collect) > 0;
    }

    /**
     * 批量更新bean
     * @param uacRoleUsers 需要更新的bean
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean updateList(List<UacRoleUser> uacRoleUsers) {
        List<UacRoleUser> collect = uacRoleUsers.stream().filter(Objects::nonNull)
                .filter(uacRoleUser -> uacRoleUser.getId() != null)
                .collect(Collectors.toList());
        return updateBatch(collect) > 0;
    }
}
