package com.example.gbdpuserac.service.impl;

import com.example.gbdpbootcore.exception.BusinessException;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpbootcore.publicToolUtil.BeanToBeanUtil;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.mapper.*;
import com.example.gbdpuserac.dto.UacRoleDto;
import com.example.gbdpuserac.model.*;
import com.example.gbdpuserac.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Service
@Transactional(readOnly = true)
public class UacRoleServiceImpl extends BaseService<UacRole> implements UacRoleService {

    @Resource
    private UacRoleMapper uacRoleMapper;
    @Resource
    private UacRoleMenuMapper uacRoleMenuMapper;
    @Resource
    private UacRoleUserMapper uacRoleUserMapper;
    @Resource
    private UacRoleOfficeMapper uacRoleOfficeMapper;
    @Resource
    private UacMenuMapper uacMenuMapper;
    @Resource
    private UacRoleMenuService uacRoleMenuService;
    @Resource
    private UacRoleOfficeService uacRoleOfficeService;
    @Resource
    private UacRoleUserService uacRoleUserService;
    @Resource
    private UacMenuService uacMenuService;
    @Resource
    private UacOfficeService uacOfficeService;

    @Override
    public PageResult listRolesByUserId(PageRequest page, String userId) {
        String order = String.format("%s %s", page.getOrderBy(), page.getOrderRule());
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), order);
        List<UacRole> select = uacRoleMapper.listRolesByUserId(userId.split(","));
        return PageResult.getPageResult(new PageInfo<>(select));
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean saveRoleAndMenu(String roleId, List<String> menuIdList) {
        // 删除就旧的中间表记录
        UacRoleMenu uacRoleMenu = new UacRoleMenu();
        uacRoleMenu.setRoleId(roleId);
        int count = uacRoleMenuMapper.selectCount(uacRoleMenu);
        int i1 = uacRoleMenuMapper.delete(uacRoleMenu);
        if (count > 0 && i1 <= 0) {
            throw new BusinessException("删除uacRoleMenu记录失败！roleId =[" + roleId + "]");
        }
        // 插入新的中间表记录
        int i = 1;
        if (!GyToolUtil.isNull(menuIdList)) {
            List<UacRoleMenu> uacRoleMenus = menuIdList.stream().filter(Objects::nonNull).map(
                    menuId -> {
                        UacRoleMenu uacRoleMenu2 = new UacRoleMenu();
                        uacRoleMenu2.setMenuId(menuId);
                        uacRoleMenu2.setRoleId(roleId);
                        uacRoleMenu2.preSave();
                        return uacRoleMenu2;
                    }
            ).collect(Collectors.toList());
            i = uacRoleMenuService.saveBatch(uacRoleMenus);
        }
        return i > 0;
    }

    @Override
    public UacRoleDto getRoleWithMenu(String id) {
        UacRole selectBean = new UacRole();
        selectBean.setId(id);
        UacRole uacRole = uacRoleMapper.selectOne(selectBean);
        if (GyToolUtil.isNull(uacRole)) {
            return null;
        }
        UacRoleDto uacRoleDto = BeanToBeanUtil.convertBean(uacRole, UacRoleDto.class);
        // 设置Menu树形结构
        List<Object> menus = uacMenuService.menuTreeByRoleId(id);
        // 设置机构树形结构
        List<Object> offices = uacOfficeService.officeTreeByRoleId(id);
        uacRoleDto.setUacMenuList(menus);
        uacRoleDto.setUacOfficeList(offices);

        return uacRoleDto;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteRoleById(String id) {
        // 删除角色关联关系
        Condition roleMenuCondition = new Condition(UacRoleMenu.class);
        Condition roleUserCondition = new Condition(UacRoleUser.class);
        Condition roleOfficeCondition = new Condition(UacRoleOffice.class);

        roleMenuCondition.createCriteria().andEqualTo("roleId", id);
        roleOfficeCondition.createCriteria().andEqualTo("roleId", id);
        roleUserCondition.createCriteria().andEqualTo("roleId", id);

        uacRoleMenuMapper.deleteByCondition(roleMenuCondition);
        uacRoleOfficeMapper.deleteByCondition(roleOfficeCondition);
        uacRoleUserMapper.deleteByCondition(roleUserCondition);

        return deleteById(id);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteRoleByIds(String idsStr) {

        uacRoleMenuMapper.deleteRoleMenuByRoleIds(idsStr.split(","));
        uacRoleOfficeMapper.deleteRoleOfficeByRoleIds(idsStr.split(","));
        uacRoleUserMapper.deleteRoleUserByRoleIds(idsStr.split(","));
        return deleteByIds(idsStr);
    }

    @Override
    public boolean checkDuplicateRole(UacRole uacRole) {
        int change = 0;
        if (!uacRole.isNew()) {
            UacRole oldRole = uacRoleMapper.selectByPrimaryKey(uacRole.getId());
            if (oldRole != null && GyToolUtil.isNotEqual(oldRole.getName(), uacRole.getName())) {
                change += 1;
            }
            if (oldRole != null && GyToolUtil.isNotEqual(oldRole.getEnname(), uacRole.getEnname())) {
                change += 2;
            }
            if (change == 0) {
                return false;
            }
        }
        // 检查name是否重复
        Condition condition = new Condition(UacRole.class);
        Example.Criteria criteria = condition.createCriteria();
                //.andEqualTo("delFlag", "0");
        if (change == 1) {
            criteria.orEqualTo("name", uacRole.getName().trim());
        }
        if (change == 2) {
            criteria.orEqualTo("enname", uacRole.getEnname().trim());
        }
        if (uacRole.isNew() || change == 3) {
            criteria.orEqualTo("name", uacRole.getName().trim())
                    .orEqualTo("enname", uacRole.getEnname().trim());
        }

        List<UacRole> tmpList = uacRoleMapper.selectByCondition(condition);
        long count = tmpList.stream().filter(
                tmpUacRole -> tmpUacRole.getDelFlag().equals("0")&&!tmpUacRole.getId().equals(uacRole.getId())).count();
        //int count = uacRoleMapper.selectCountByCondition(condition);
        System.out.println(count);
        return count > 0;

    }

    /**
     * 保存角色和用户关联
     *
     * @param roleId  角色ID
     * @param userIds 用户ids
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean saveRoleAndUser(String roleId, String... userIds) {
        // 查询当前所有的role-user记录
        UacRoleUser select = new UacRoleUser();
        select.setRoleId(roleId);
        List<UacRoleUser> list = uacRoleUserService.list(select);
        List<String> oldUidList = list.stream().filter(Objects::nonNull).map(UacRoleUser::getUserId).collect(Collectors.toList());

        List<UacRoleUser> uacRoleUserList = new ArrayList<>();
        for (String userId : userIds) {
            // 过滤已经添加的记录
            if (!oldUidList.contains(userId)) {
                UacRoleUser uacRoleUser = new UacRoleUser();
                uacRoleUser.setUserId(userId).setRoleId(roleId);
                uacRoleUser.preSave();
                uacRoleUserList.add(uacRoleUser);
            }
        }
        return uacRoleUserService.saveBatch(uacRoleUserList) > 0;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean saveRoleAndOffice(String roleId, List<String> officeIdList) {
        // 先删除旧的关联
        UacRoleOffice select = new UacRoleOffice();
        select.setRoleId(roleId);
        int count = uacRoleOfficeMapper.selectCount(select);
        int i1 = uacRoleOfficeMapper.delete(select);
        if (count > 0 && i1 <= 0) {
            throw new BusinessException("删除uacRoleRole记录失败！roleId =[" + roleId + "]");
        }
        // 插入新的关联
        int i = 1;
        if (!CollectionUtils.isEmpty(officeIdList)) {
            List<UacRoleOffice> uacRoleOffices = officeIdList.stream().filter(Objects::nonNull).map(
                    officeId -> {
                        UacRoleOffice roleOffice = new UacRoleOffice();
                        roleOffice.setRoleId(roleId);
                        roleOffice.setOfficeId(officeId);
                        roleOffice.preSave();
                        return roleOffice;
                    }
            ).collect(Collectors.toList());
            i = uacRoleOfficeService.saveBatch(uacRoleOffices);
        }
        return i > 0;
    }

    @Override
    public boolean checkRoleExist(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return false;
        }
        return uacRoleMapper.existsWithPrimaryKey(roleId);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean deleteRoleAndUser(String roleId, String... userIds) {
        for (String userId : userIds) {
            UacRoleUser uacRoleUser = new UacRoleUser();
            uacRoleUser.setUserId(userId).setRoleId(roleId);
            uacRoleUserMapper.delete(uacRoleUser);
        }
        return true;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean saveOrUpdateRole(UacRole uacRole) {
        boolean isNew = uacRole.isNew();
        if (isNew) {
            uacRole.preSave();
        }
        // 设置menu-role映射
        if (!CollectionUtils.isEmpty(uacRole.getUacActions())) {
            saveRoleAndMenu(uacRole.getId(), uacRole.getUacActions());
        }
        return isNew ? uacRoleMapper.insertSelective(uacRole) > 0 :
                uacRoleMapper.updateByPrimaryKeySelective(uacRole) > 0;
    }
}
