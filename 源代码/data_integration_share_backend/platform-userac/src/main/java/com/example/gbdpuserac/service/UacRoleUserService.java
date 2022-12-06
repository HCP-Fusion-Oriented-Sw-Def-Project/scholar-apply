package com.example.gbdpuserac.service;



import com.example.gbdpbootcore.core.Service;
import com.example.gbdpuserac.model.UacRoleUser;

import java.util.List;

/**
 * <p>
 * 角色用户中间表 服务类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
public interface UacRoleUserService extends Service<UacRoleUser> {

    boolean saveList(String id, List<String> roleIdList);

    boolean updateList(List<UacRoleUser> deletedRoleUserList);
}
