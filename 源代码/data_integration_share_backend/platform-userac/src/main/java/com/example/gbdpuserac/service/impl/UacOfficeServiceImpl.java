package com.example.gbdpuserac.service.impl;

import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpbootcore.publicToolUtil.BeanToBeanUtil;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.mapper.UacOfficeMapper;
import com.example.gbdpuserac.mapper.UacRoleOfficeMapper;
import com.example.gbdpuserac.mapper.UacUserOfficeMapper;
import com.example.gbdpuserac.dto.UacOfficeDto;
import com.example.gbdpuserac.model.UacOffice;
import com.example.gbdpuserac.model.UacRole;
import com.example.gbdpuserac.model.UacRoleOffice;
import com.example.gbdpuserac.model.UacUserOffice;
import com.example.gbdpuserac.service.UacOfficeService;
import com.example.gbdpuserac.service.UacUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 机构表 服务实现类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Service
@Transactional(readOnly = true)
@Slf4j
public class UacOfficeServiceImpl extends BaseService<UacOffice> implements UacOfficeService {

    @Resource
    private UacOfficeMapper uacOfficeMapper;
    @Resource
    private UacRoleOfficeMapper uacRoleOfficeMapper;
    @Resource
    private UacUserOfficeMapper uacUserOfficeMapper;
    @Resource
    private UacUserService uacUserService;

    /**
     *@Description 保存 Office
     *@return boolean
     *@Author kwc
     *@date 2019/12/19
     * @param uacOffice
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean saveOffice(UacOffice uacOffice) {
        // 填充parent_ids字段
        String parentId = uacOffice.getParentId();
        UacOffice officeParent = uacOfficeMapper.selectByPrimaryKey(parentId);
        uacOffice.setParentIds(officeParent.getParentIds() + parentId + ",");
        if (StringUtils.isEmpty(uacOffice.getUseAble())){
            // 设置able默认值
            uacOffice.setUseAble("1");
        }
        if (uacOffice.getSort() == null){
            // 设置sort默认值
            uacOffice.setSort(999);
        }
        uacOffice.setGrade(String.valueOf(Integer.parseInt(officeParent.getGrade()) + 1));
        uacOffice.preSave();
        int save = super.save(uacOffice);
        // todo 缓存修改
        return save > 0;

        // todo 保存role数据:新建机构是否给其赋值角色
        /*List<UacRoleDto> uacRoleDtoList = uacOfficeDto.getUacRoleDtoList();
        if (!GyToolUtil.isNull(uacRoleDtoList)){
            List<UacRoleOffice> uacRoleOffices = uacRoleDtoList.stream().filter(Objects::nonNull).map(
                    uacRoleDto -> {
                        UacRoleOffice uacRoleOffice = new UacRoleOffice();
                        uacRoleOffice.setRoleId(uacRoleDto.getId());
                        uacRoleOffice.setOfficeId(uacOffice.getId());
                        uacRoleOffice.preSave();
                        return uacRoleOffice;
                    }
            ).collect(Collectors.toList());
            int i1 = uacRoleOfficeMapper.insertList(uacRoleOffices);
            return i1 > 0;
        }*/
    }

    /**
     *@Description
     *@return
     *@Author kwc
     *@date 2019/12/19
     * @param
     * @param uacOffice
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean updateOffice(UacOffice uacOffice) {
        // 填充parent_ids字段
        String parentId = uacOffice.getParentId();
        UacOffice officeParent = uacOfficeMapper.selectByPrimaryKey(parentId);
        uacOffice.setParentIds(officeParent.getParentIds() + parentId + ",");
        uacOffice.setGrade(String.valueOf(Integer.parseInt(officeParent.getGrade()) + 1));
        UacOffice selectBean = new UacOffice();
        selectBean.setId(uacOffice.getId());
        UacOffice oldOffice = uacOfficeMapper.selectByPrimaryKey(uacOffice.getId());
        String oldParentIds = oldOffice.getParentIds();
        // parent_ids字段是否修改
        if (oldParentIds.equals(uacOffice.getParentIds())) {
            Condition officeCondition = new Condition(UacOffice.class);
            officeCondition.createCriteria().andLike("parentIds", "%,"+uacOffice.getId()+",%");
            List<UacOffice> uacOffices = uacOfficeMapper.selectByCondition(officeCondition);
            for (UacOffice child : uacOffices){
                child.setParentIds(child.getParentIds().replace(oldParentIds, uacOffice.getParentIds()));
                child.setGrade(String.valueOf(Integer.parseInt(uacOffice.getGrade() + 1)));
                uacOfficeMapper.updateByPrimaryKeySelective(child);
            }
        }
        uacOffice.preUpdate();
        int save = super.update(uacOffice);
        return save > 0;
        // todo 缓存修改
    }

    /**
     *@Description 排序返回所有UacOffice
     *@param
     *@return List<UacOffice>
     *@Author kwc
     *@date 2019/12/19
     */
    @Override
    public List<UacOffice> listAllOffice() {
        UacOffice uacOffice = new UacOffice();
        //数据权限
        //uacOffice.getSqlMap().put("dsSql", BaseService.dataScopeFilter(getUserInfo(), "", "uu,pp,dp"));
        List<UacOffice> uacOffices = uacOfficeMapper.listPage(uacOffice);
        List<UacOffice> resultList = new ArrayList<>();
        // todo 重新排序 默认最高级菜单id为0
        UacOffice.treeArraySort(resultList, uacOffices, "0");
        return resultList;
    }

    /**
     *@Description 获取封装一个绑定了role信息的office
     *@param  id officeId
     *@return UacOfficeDto
     *@Author kwc
     *@date 2019/12/19
     */
    @Override
    public UacOfficeDto getOfficeWithRole(String id) {
        UacOffice office = uacOfficeMapper.get(id);
        if (GyToolUtil.isNull(office)) {
            return null;
        }
        UacOfficeDto uacOfficeDto = BeanToBeanUtil.convertBean(office, UacOfficeDto.class);
        List<UacRole> uacRoles = uacOfficeMapper.listRoleByOfficeId(id);
        uacOfficeDto.setUacRoleList(uacRoles);
        return uacOfficeDto;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteOfficeById(String id) {
        // 删除中间表记录
        UacRoleOffice uacRoleOffice = new UacRoleOffice();
        uacRoleOffice.setOfficeId(id);
        uacRoleOfficeMapper.delete(uacRoleOffice);
        UacUserOffice uacUserOffice = new UacUserOffice();
        uacUserOffice.setOfficeId(id);
        uacUserOfficeMapper.delete(uacUserOffice);

        return deleteById(id);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteOfficeByIds(String ids) {

        // 删除中间表记录
        uacRoleOfficeMapper.deleteRoleOfficeByOfficeIds(ids.split(","));
        uacUserOfficeMapper.deleteByOfficeIds(ids.split(","));

        return deleteByIds(ids);
    }

    /**
     *@Description 树形结构返回所有UacOffice
     *@param
     *@return List<Object>
     *@Author kwc
     *@date 2020/4/4
     */
    @Override
    public List<Object> officeTree() {
        UacOffice uacOffice = new UacOffice();
        PageHelper.orderBy("sort");
        //uacOffice.getSqlMap().put("dsSql", BaseService.dataScopeFilter(getUserInfo(), "", "uu,pp,dp"));
        List<UacOffice> offices = uacOfficeMapper.listPage(uacOffice);
        return UacOffice.officeTree(offices, new ArrayList<>());
    }

    @Override
    public List<Object> officeTreeByRoleId(String roleId) {

        // 所有机构列表
        PageHelper.orderBy("sort");
        UacOffice uacOffice = new UacOffice();
        //uacOffice.getSqlMap().put("dsSql", BaseService.dataScopeFilter(getUserInfo(), "", "uu,pp,dp"));
        List<UacOffice> allOffices = uacOfficeMapper.listPage(uacOffice);

        UacRoleOffice uacRoleOffice = new UacRoleOffice();
        uacRoleOffice.setRoleId(roleId);
        List<UacRoleOffice> select = uacRoleOfficeMapper.select(uacRoleOffice);
        // 角色对应office
        List<String> officeIdsByRoleId = select.stream().map(UacRoleOffice::getOfficeId).collect(Collectors.toList());

        return UacOffice.officeTree(allOffices, officeIdsByRoleId);
    }

    /**
     * 通过roleId查询所有绑定的office 分页+筛选
     *
     * @param roleId
     * @param uacOffice
     * @param pageRequest
     * @return
     */
    @Override
    public PageResult getOfficeByRoleId(String roleId, UacOffice uacOffice, PageRequest pageRequest) {
        String order = String.format("%s %s", pageRequest.getOrderBy(), pageRequest.getOrderRule());
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize(), order);
        //连表查询

        List<UacOffice> uacOffices = uacOfficeMapper.getOfficeByRoleId(roleId, uacOffice);
        return PageResult.getPageResult(new PageInfo<>(uacOffices));
    }

    @Override
    public List<String> filterDisableOffice(List<String> officeIdList) {
        if (CollectionUtils.isEmpty(officeIdList)) {
            return Collections.EMPTY_LIST;
        }
        return officeIdList.stream().filter(
                officeId -> {
                    UacOffice uacOffice = mapper.selectByPrimaryKey(officeId);
                    return uacOffice != null && "1".equals(uacOffice.getUseAble());
                }
        ).collect(Collectors.toList());
    }

    /**
     * 一个父节点下不能出现同名节点
     *
     * @param uacOffice
     * @return
     */
    @Override
    public boolean checkDuplicateOffice(UacOffice uacOffice) {
        // 更新操作需要判断
        if (!uacOffice.isNew()) {
            UacOffice oldOffice = uacOfficeMapper.selectByPrimaryKey(uacOffice.getId());
            // 如果父节点和名称都没改就不需要查库
            if (oldOffice != null && GyToolUtil.isEqual(uacOffice.getParentId(), oldOffice.getParentId()) &&
                    GyToolUtil.isEqual(uacOffice.getName(), oldOffice.getName())) {
                return false;
            }
        }
        Condition condition = new Condition(UacOffice.class);
        condition.createCriteria()
                .andEqualTo("delFlag", "0")
                .andEqualTo("name", uacOffice.getName().trim())
                .andEqualTo("parentId", uacOffice.getParentId().trim());
        int i = uacOfficeMapper.selectCountByCondition(condition);
        return i > 0;
    }

    @Override
    public boolean hasChild(String... ids) {
        for (String id : ids) {
            // 查询父节点为当前节点的节点
            Condition officeCondition = new Condition(UacOffice.class);
            officeCondition.createCriteria().andLike("parentIds", "%," + id + ",%").andEqualTo("delFlag","0");
            List<UacOffice> uacOffices = uacOfficeMapper.selectByCondition(officeCondition);
            if (!CollectionUtils.isEmpty(uacOffices)) {
                return true;
            }
        }
        return false;
    }
}
