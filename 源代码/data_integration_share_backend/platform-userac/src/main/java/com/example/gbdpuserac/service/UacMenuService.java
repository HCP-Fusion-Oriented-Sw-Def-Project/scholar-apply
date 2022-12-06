package com.example.gbdpuserac.service;

import com.example.gbdpbootcore.core.Service;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpuserac.model.UacMenu;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
public interface UacMenuService extends Service<UacMenu> {

    List<String> listMenuPerByUserId(String userId);
    /**
     * @version 1.0
     * @MethodName listMenuByRoleIds
     * @Description 查询当前角色下的菜单
     * @Author lhf
     * @Date 2019-12-14 23:33
     **/
    PageResult listByRoleId(PageRequest pageRequest, List<String> roleids);

    /**
     * 树形排序 返回当前所有的菜单（权限）
     * @return List<UacMenu>
     */
    List<UacMenu> listAllMenu();

    /**
     * 树形结构 返回当前所有的菜单（权限）
     * @return List<Object>
     */
    List<Object> menuTree();

    @Transactional(readOnly = false)
    int saveMenu(UacMenu uacMenu);

    int updateMenu(UacMenu uacMenu);

    int deleteMenuById(String id);

    int deleteMenuByIds(String ids);

    boolean hasChild(String... id);

    boolean checkDuplicateMenu(UacMenu uacMenu);

    /**
     * 树形结构 返回当前所有的菜单（权限）
     * @return List<Object>
     */
    List<Object> menuTreeByRoleId(String roleId);

}
