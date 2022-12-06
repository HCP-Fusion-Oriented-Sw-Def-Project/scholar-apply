package com.example.gbdpuserac.service.impl;

import com.example.gbdpbootcore.annotation.DataScopePermission;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.mapper.UacMenuMapper;
import com.example.gbdpuserac.mapper.UacRoleMenuMapper;
import com.example.gbdpuserac.service.UacMenuService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import com.example.gbdpuserac.model.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class UacMenuServiceImpl extends BaseService<UacMenu> implements UacMenuService {

    @Resource
    private UacMenuMapper uacMenuMapper;
    @Resource
    private UacRoleMenuMapper uacRoleMenuMapper;

    @Override
    public List<String> listMenuPerByUserId(String userId){
        return uacMenuMapper.listMenuPerByUserId(userId);
    }

    /**
     * todo 这个方法需要改
     * 返回pageRequest是否合理？
     *
     * @param pageRequest
     * @param roleIds
     * @return
     */
    @Override
    public PageResult listByRoleId(PageRequest pageRequest, List<String> roleIds){
        String order = String.format("%s %s", pageRequest.getOrderBy(), pageRequest.getOrderRule());
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize(), order);
        List<UacMenu> select = uacMenuMapper.listMenuByRoleIds(roleIds);
        return PageResult.getPageResult(new PageInfo<>(select));
    }

    /**
     *@Description
     *@param
     *@return
     *@Author kwc
     *@date
     */
    @Override
    @DataScopePermission
    public List<UacMenu> listAllMenu() {
        Condition condition = new Condition(UacMenu.class);
        condition.setOrderByClause("sort");
        condition.createCriteria().andEqualTo("delFlag", "0");
        List<UacMenu> menuList = uacMenuMapper.selectByCondition(condition);
        List<UacMenu> resultList = new ArrayList<>();
        // todo 重新排序 默认最高级菜单id为0
        UacMenu.treeArraySort(resultList, menuList, "0");
        return resultList;
    }

    @Override
    public List<Object> menuTree() {
        Condition condition = new Condition(UacMenu.class);
        condition.setOrderByClause("sort");
        condition.createCriteria().andEqualTo("delFlag", "0");
        List<UacMenu> menuList = uacMenuMapper.selectByCondition(condition);
        return UacMenu.menuTree(menuList, new ArrayList<>());
    }

    @Override
    public List<Object> menuTreeByRoleId(String roleId) {
        // 查询所有菜单记录
        Condition condition = new Condition(UacMenu.class);
        condition.setOrderByClause("sort");
        condition.createCriteria().andEqualTo("delFlag", "0");
        List<UacMenu> allMenuList = uacMenuMapper.selectByCondition(condition);
        // 查询当前角色对应菜单
        UacRoleMenu uacRoleMenu = new UacRoleMenu();
        uacRoleMenu.setRoleId(roleId);
        List<UacRoleMenu> select = uacRoleMenuMapper.select(uacRoleMenu);
        List<String> allMenuIdByRoleId = select.stream().map(UacRoleMenu::getMenuId).collect(Collectors.toList());
        return UacMenu.menuTree(allMenuList, allMenuIdByRoleId);
    }

    /**
     *@Description save Menu
     *@param
     *@return
     *@Author kwc
     *@date
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int saveMenu(UacMenu uacMenu) {
        // 填充parentIds字段
        String parentId = uacMenu.getParentId();
        UacMenu parent = getById(parentId);
        String parentIds = parent.getParentIds();
        uacMenu.setParentIds(parentIds + parentId + ",");
        // 填充level字段
        uacMenu.setLevel(parent.getLevel() + 1);
        uacMenu.preSave();
        // todo 菜单缓存修改
        return uacMenuMapper.insert(uacMenu);
    }

    /**
     *@Description
     *@param
     *@return
     *@Author kwc
     *@date
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int updateMenu(UacMenu uacMenu) {
        UacMenu selectBean = new UacMenu();
        selectBean.setId(uacMenu.getId());
        UacMenu oldMenuBean = uacMenuMapper.selectOne(selectBean);
        String oldParentId = oldMenuBean.getParentId();
        String newParentId = uacMenu.getParentId();
        UacMenu newParent = getById(newParentId);
        // 更新level数据
        uacMenu.setLevel(newParent.getLevel() + 1);
        // parent 有修改
        if (!oldParentId.equals(newParentId)) {
            uacMenu.setParentIds(newParent.getParentIds() + newParentId + ",");
            // 获取修改前的parentIds，用于更新子节点的parentIds
            String oldParentIds = oldMenuBean.getParentIds();
            // 更新子节点 parentIds
            Condition condition = new Condition(UacMenu.class);
            condition.createCriteria().andLike("parentIds", "%,"+uacMenu.getId()+",%");
            List<UacMenu> menuList = uacMenuMapper.selectByCondition(condition);
            for (UacMenu child : menuList){
                child.setParentIds(child.getParentIds().replace(oldParentIds, uacMenu.getParentIds()));
                child.setLevel(uacMenu.getLevel() + 1);
                uacMenuMapper.updateByPrimaryKeySelective(child);
            }
        }
        uacMenu.preUpdate();
        // todo 缓存修改
        return super.update(uacMenu);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteMenuById(String id) {
        // 删除中间表记录
        UacRoleMenu uacRoleMenu = new UacRoleMenu();
        uacRoleMenu.setMenuId(id);
        uacRoleMenuMapper.delete(uacRoleMenu);

        return deleteById(id);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteMenuByIds(String ids) {
        // 删除中间表记录
        uacRoleMenuMapper.deleteRoleMenuByMenuIds(ids.split(","));

        return deleteByIds(ids);
    }

    /**
     * 查询当前节点是否有子子节点
     * @param ids
     * @return
     */
    @Override
    public boolean hasChild(String... ids) {
        for (String id : ids) {
            // 查询父节点为当前节点的节点
            Condition condition = new Condition(UacMenu.class);
            condition.createCriteria().andLike("parentIds", "%,"+ id + ",%");
            List<UacMenu> menuList = uacMenuMapper.selectByCondition(condition);
            if (!CollectionUtils.isEmpty(menuList)) {
                return true;
            }
        }
       return false;
    }

    /**
     * 参数重复性校验
     *
     * @param uacMenu
     * @return
     */
    @Override
    public boolean checkDuplicateMenu(UacMenu uacMenu) {
        // update / insert
        if (uacMenu.isNew()) {
            // 参数重复性校验
            String name = uacMenu.getName();
            String url = uacMenu.getUrl();
            String permission = uacMenu.getPermission() == null ? "" : uacMenu.getPermission();

            Condition condition = new Condition(UacMenu.class);
            Example.Criteria criteria = condition.createCriteria()
                    .andEqualTo("delFlag", "0")
                    .orEqualTo("name", name.trim());
            if (!StringUtils.isEmpty(permission)) {
                criteria = criteria.orEqualTo("permission", permission.trim());
            }
            if (!StringUtils.isEmpty(url)) {
                criteria = criteria.orEqualTo("url", url.trim());
            }

            int i = uacMenuMapper.selectCountByCondition(condition);

            return i > 0;
        }
        UacMenu oldMenu = uacMenuMapper.selectByPrimaryKey(uacMenu.getId());
        if (oldMenu != null && GyToolUtil.isEqual(oldMenu.getUrl(), uacMenu.getUrl())
                && GyToolUtil.isEqual(oldMenu.getName(), uacMenu.getName())
                && GyToolUtil.isEqual(oldMenu.getPermission(), uacMenu.getPermission())) {
            return false;
        }
        Condition condition1 = new Condition(UacMenu.class);
        Example.Criteria criteria = condition1.createCriteria();
        criteria.andEqualTo("delFlag", "0");
        if (GyToolUtil.isNotEqual(oldMenu.getName(), uacMenu.getName())) {
            criteria.orEqualTo("name", uacMenu.getName());
        }
        if (GyToolUtil.isNotEqual(oldMenu.getUrl(), uacMenu.getUrl())) {
            criteria.orEqualTo("url", uacMenu.getUrl());
        }
        if (GyToolUtil.isNotEqual(oldMenu.getPermission(), uacMenu.getPermission())) {
            criteria.orEqualTo("permission", uacMenu.getPermission());
        }
        int i = uacMenuMapper.selectCountByCondition(condition1);
        return i > 0;
    }


}
