package com.example.gbdpuserac.service.impl;

import com.example.gbdpbootcore.dto.ResetPasswordDto;
import com.example.gbdpbootcore.enums.ErrorCodeEnum;
import com.example.gbdpbootcore.exception.BusinessException;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpbootcore.publicToolUtil.BeanToBeanUtil;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpbootcore.publicToolUtil.IPUtil;
import com.example.gbdpbootcore.util.PublicUtil;
import com.example.gbdpbootcore.util.SendEmailUtil;
import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.mapper.*;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.model.*;
import com.example.gbdpuserac.model.enums.UacUserSourceEnum;
import com.example.gbdpuserac.model.enums.UacUserStatusEnum;
import com.example.gbdpuserac.security.Md5Util;
import com.example.gbdpuserac.security.SecurityUser;
import com.example.gbdpuserac.service.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.gbdpuserac.config.DateProcess.mergeStr;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Service
@Slf4j
@Transactional(readOnly = true)
public class UacUserServiceImpl extends BaseService<UacUser> implements UacUserService {

    @Resource
    private UacUserMapper uacUserMapper;
    @Resource
    private UacMenuMapper uacMenuMapper;
    @Resource
    private UacOfficeService uacOfficeService;
    @Resource
    private UacRoleMapper uacRoleMapper;
    @Resource
    private UacOfficeMapper uacOfficeMapper;
    @Resource
    private UacUserOfficeService uacUserOfficeService;
    @Resource
    private UacRoleUserService uacRoleUserService;
    @Resource
    private UacRoleUserMapper uacRoleUserMapper;
    @Resource
    private UacUserOfficeMapper uacUserOfficeMapper;

    @Resource
    private UacMenuService uacMenuService;

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public UacUser getByLoginName(String loginName) {
        log.info("getByLoginName - 根据用户名查询用户信息. loginName={}", loginName);
        return uacUserMapper.getByLoginName(loginName);
    }

    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public UacUser getUacUserByUserCode(String userCode) {
        return uacUserMapper.getUacUserByUserCode(userCode);
    }


    @Override
    public UacUserDto getUserInfoList(String username) throws UsernameNotFoundException {
        UacUser user = uacUserMapper.getByLoginName(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户名不存在或者密码错误");
        }
        UacUserDto userInfo = BeanToBeanUtil.convertBean(user, UacUserDto.class);

        //获取角色列表
        List<UacRole> ownRoleList = uacRoleMapper.listRolesByUserId(new String[]{user.getId()});
        // 获取用户获取对应的菜单并集 todo SQL待测
        List<UacMenu> menuPermList = uacMenuMapper.listMenuByUserId(user.getId());
        // 获取用户的部门列表
        List<UacOffice> officeList = uacOfficeMapper.listOfficeByUserId(user.getId());

        userInfo.setMenuList(menuPermList);
        userInfo.setRoleList(ownRoleList);
        userInfo.setOfficeList(officeList);
        return userInfo;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean updatePassword(ResetPasswordDto resetPasswordDto) {
        if (resetPasswordDto == null
                || StringUtils.isEmpty(resetPasswordDto.getPasswordNew())) {
            return false;
        }
        // 忘记密码情况下的修改密码
        if (resetPasswordDto.isForgetPassword()) {
            // todo 邮件验证码验证
            String passwordNew = resetPasswordDto.getPasswordNew();
            UacUser uacUser = getOne(new UacUser().setEmail(resetPasswordDto.getEmail()));
            int update = update(uacUser.setPassword(Md5Util.encrypt(passwordNew)));
            return update >= 1;
        }

        // 用户登陆情况下修改自己密码
        if (StringUtils.isEmpty(resetPasswordDto.getLoginName())
                || StringUtils.isEmpty(resetPasswordDto.getPasswordOld())) {
            return false;
        }
        String passwordOld = resetPasswordDto.getPasswordOld();
        String passwordNew = resetPasswordDto.getPasswordNew();
        String userName = resetPasswordDto.getLoginName();
        UacUser selectEntity = new UacUser();
        selectEntity.setLoginName(userName);
        List<UacUser> uacUsers = uacUserMapper.select(selectEntity);
        UacUser uacUser = uacUsers.get(0);
        if (uacUser != null &&
                uacUser.getPassword().equals(Md5Util.encrypt(passwordOld))) {
            int update = update(uacUser.setPassword(Md5Util.encrypt(passwordNew)));
            if (update < 1) {
                return false;
            }
        }
        return false;
    }

    /**
     * 后台新建用户
     *
     * @param uacUserDto
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean saveUser(UacUserDto uacUserDto) {
        if (GyToolUtil.isNull(uacUserDto)) {
            throw new BusinessException(ErrorCodeEnum.TPC10050005);
        }
        // 参数校验
        validateRegisterInfo(uacUserDto);
        UacUser uacUser = BeanToBeanUtil.convertBean(uacUserDto, UacUser.class);
        uacUser.preSave();
        uacUser.setPassword(Md5Util.encrypt(uacUserDto.getLoginPwd()));
        uacUser.setUserSource(UacUserSourceEnum.INSERT.getKey());
        uacUser.setStatus(UacUserStatusEnum.ENABLE.getKey());
        uacUser.setLastLoginTime(new Date());
        log.info("新建用户. user={}", uacUser);
        int insert = uacUserMapper.insert(uacUser);
        uacUserDto.setId(uacUser.getId());
        //新建用户需要同时绑定角色和绑定部门
        boolean b = upsertUserRelateList(uacUserDto);
        return insert > 0 && b;
    }

    @Override
    public UacUserDto getUserDto(String userId) {

        UacUser uacUser = uacUserMapper.get(userId);
        UacUserDto uacUserDto = BeanToBeanUtil.convertBean(uacUser, UacUserDto.class);
        if (uacUser ==  null) {
            return null;
        }
        //获取角色列表
        List<UacRole> ownRoleList = uacRoleMapper.listRolesByUserId(new String[]{uacUserDto.getId()});
        // 获取用户获取对应的菜单并集 todo SQL待测
        List<UacMenu> menuPermList = uacMenuMapper.listMenuByUserId(uacUserDto.getId());
        // 获取用户的部门列表
        List<UacOffice> officeList = uacOfficeMapper.listOfficeByUserId(uacUserDto.getId());

        uacUserDto.setMenuList(menuPermList);
        uacUserDto.setRoleList(ownRoleList);
        uacUserDto.setOfficeList(officeList);
        return uacUserDto;
    }

    /**
     * 通过roleId获取所有的用户（分页+筛选）
     *
     * @param roleId
     * @param uacUser
     * @param pageRequest
     * @return
     */
    @Override
    public PageResult getUserByRoleId(String roleId, UacUser uacUser, PageRequest pageRequest) {
        String order = String.format("%s %s", pageRequest.getOrderBy(), pageRequest.getOrderRule());
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize(), order);
        //连表查询
        List<UacUser> uacUsers = uacUserMapper.getUserByRoleId(roleId, uacUser);
        return PageResult.getPageResult(new PageInfo<>(uacUsers));
    }

    /**
     * 通过officeId获取所有的用户（分页+筛选）
     *
     * @param officeId
     * @param uacUser
     * @param pageRequest
     * @return
     */
    @Override
    public PageResult getUserByOfficeId(String officeId, UacUser uacUser, PageRequest pageRequest) {
        String order = String.format("%s %s", pageRequest.getOrderBy(), pageRequest.getOrderRule());
        PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize(), order);
        //连表查询
        List<UacUser> uacUsers = uacUserMapper.getUserByOfficeId(officeId, uacUser);
        return PageResult.getPageResult(new PageInfo<>(uacUsers));
    }

    @Override
    public boolean disableUser(String userId) {
        UacUser uacUser = new UacUser();
        uacUser.setId(userId);
        uacUser.setStatus("DISABLE");
        uacUser.preUpdate();
        return uacUserMapper.updateByPrimaryKeySelective(uacUser) > 0;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteUserById(String id) {
        // 删除中间表记录
        Condition userOfficeCondition = new Condition(UacUserOffice.class);
        Condition roleUserCondition = new Condition(UacRoleUser.class);
        userOfficeCondition.createCriteria().andEqualTo("userId", id);
        roleUserCondition.createCriteria().andEqualTo("userId", id);
        uacUserOfficeMapper.deleteByCondition(userOfficeCondition);
        uacRoleUserMapper.deleteByCondition(roleUserCondition);

        return deleteById(id);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public int deleteUserByIds(String ids) {
        // 删除中间表记录
        uacUserOfficeMapper.deleteUserOfficeByUserIds(ids.split(","));
        uacRoleUserMapper.deleteRoleUserByUserIds(ids.split(","));

        return deleteByIds(ids);
    }

    /**
     * 校验密码强度
     * @param loginPwd
     * @return
     */
    @Override
    public boolean checkPasswordStrength(String loginPwd) {
        // 1.验证长度
        if (StringUtils.isEmpty(loginPwd) || loginPwd.trim().length() < 8) {
            return false;
        }
        // 2。验证强度
        return loginPwd.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*?&<>=+-])[A-Za-z\\d$@$!%*?&<>=+-]{8,}$");
    }

    @Override
    public boolean checkUserExist(String... userIds) {
        for (String userId : userIds) {
            if (StringUtils.isEmpty(userId)) {
                return false;
            }
            if (!uacUserMapper.existsWithPrimaryKey(userId)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkDuplicateUser(UacUserDto uacUserDto) {
        if (uacUserDto.isNew()) {
//            Condition condition = new Condition(UacUser.class);
//            condition.createCriteria().orEqualTo("loginName", uacUserDto.getLoginName().trim())
//                    .orEqualTo("phone", uacUserDto.getPhone().trim())
//                    .orEqualTo("email", uacUserDto.getEmail().trim());
//            int count = uacUserMapper.selectCountByCondition(condition);
            int count = selectCountDuplicate(uacUserDto.getLoginName().trim(),
                    uacUserDto.getPhone().trim(),uacUserDto.getEmail().trim());
            return count > 0;
        }
        UacUser oldUser = uacUserMapper.selectByPrimaryKey(uacUserDto.getId());
        if (oldUser != null && GyToolUtil.isEqual(oldUser.getLoginName(), uacUserDto.getLoginName())
                && GyToolUtil.isEqual(oldUser.getPhone(), uacUserDto.getPhone())
                && GyToolUtil.isEqual(oldUser.getEmail(), uacUserDto.getEmail())) {
            return false;
        }
//        Condition condition = new Condition(UacUser.class);
//        Example.Criteria criteria = condition.createCriteria();
        String loginName=null,phone=null,email = null;
        if (GyToolUtil.isNotEqual(oldUser.getLoginName(), uacUserDto.getLoginName())){
            //criteria.orEqualTo("loginName", uacUserDto.getLoginName());
            loginName = uacUserDto.getLoginName();
        }
        if (GyToolUtil.isNotEqual(oldUser.getPhone(), uacUserDto.getPhone())){
            //criteria.orEqualTo("phone", uacUserDto.getPhone());
            phone = uacUserDto.getPhone();
        }
        if (GyToolUtil.isNotEqual(oldUser.getEmail(), uacUserDto.getEmail())){
            //criteria.orEqualTo("email", uacUserDto.getEmail());
            email = uacUserDto.getEmail();
        }
        //int count = uacUserMapper.selectCountByCondition(condition);
        int count = selectCountDuplicate(loginName, phone,email);
        return count > 0;
    }

    @Override
    public boolean checkDuplicateLoginName(UacUserDto uacUserDto){
        if (uacUserDto.isNew()) {
            Condition condition = new Condition(UacUser.class);
            condition.createCriteria().andEqualTo("loginName", uacUserDto.getLoginName().trim())
                    .andCondition("del_flag='0'");
            int count = uacUserMapper.selectCountByCondition(condition);
            return count > 0;
        }
        Condition condition = new Condition(UacUser.class);
        condition.createCriteria().andEqualTo("loginName", uacUserDto.getLoginName().trim())
                .andCondition("del_flag='0'").andNotEqualTo("id",uacUserDto.getId().trim());

        int count = uacUserMapper.selectCountByCondition(condition);
        return count > 0;
    }

    @Override
    public boolean checkDuplicateEmail(UacUserDto uacUserDto){
        if (uacUserDto.isNew()) {
            Condition condition = new Condition(UacUser.class);
            condition.createCriteria().andEqualTo("email", uacUserDto.getEmail().trim())
                    .andCondition("del_flag='0'");
            int count = uacUserMapper.selectCountByCondition(condition);
            return count > 0;
        }
        Condition condition = new Condition(UacUser.class);
        condition.createCriteria().andEqualTo("email", uacUserDto.getEmail().trim())
                .andCondition("del_flag='0'").andNotEqualTo("id",uacUserDto.getId().trim());

        int count = uacUserMapper.selectCountByCondition(condition);
        return count > 0;

    }

    @Override
    public boolean checkDuplicatePhone(UacUserDto uacUserDto){
        if (uacUserDto.isNew()) {
            Condition condition = new Condition(UacUser.class);
            condition.createCriteria().andEqualTo("phone", uacUserDto.getPhone().trim())
                    .andCondition("del_flag='0'");
            int count = uacUserMapper.selectCountByCondition(condition);
            return count > 0;
        }
        Condition condition = new Condition(UacUser.class);
        condition.createCriteria().andEqualTo("phone", uacUserDto.getPhone().trim())
                .andCondition("del_flag='0'").andNotEqualTo("id",uacUserDto.getId().trim());

        int count = uacUserMapper.selectCountByCondition(condition);
        return count > 0;
    }

    /**
     * 可以支持Office和role的筛选搜索方法
     *
     * @param page
     * @param uacUser
     * @return
     */
    @Override
    public PageResult<UacUser> findUser(PageRequest page, UacUser uacUser) {
        String order = String.format("%s %s", page.getOrderBy(), page.getOrderRule());
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), order);
        List<UacUser> select = uacUserMapper.findUser(uacUser);
        // todo 过滤掉专家用户？ 这里应该有问题，一个用户绑定两个角色回出现重复数据
        /*String roleId = "aa2177d2-9400-4696-bdcb-130bd3df8569";
        List<UacUser> uacUsers = uacUserMapper.getUserByRoleId(roleId, uacUser);
        List<UacUser> collect = select.stream().filter(
                user -> !uacUsers.contains(user)
        ).collect(Collectors.toList());*/
        return PageResult.getPageResult(new PageInfo<>(select));
    }

    /**
     * 用户信息更新
     *
     * @param uacUserDto 改后用户信息包含用户uid
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean updateUser(UacUserDto uacUserDto) {
        // 更新用户表信息
        log.info("更新用户信息 uacUserDto={}", uacUserDto);
        UacUser uacUser = BeanToBeanUtil.convertBean(uacUserDto, UacUser.class);
        if (StringUtils.isNotEmpty(uacUserDto.getLoginPwd())
                && GyToolUtil.isEqual(uacUserDto.getLoginPwd(), uacUserDto.getConfirmPwd())) {
            uacUser.setPassword(Md5Util.encrypt(uacUserDto.getLoginPwd()));
        }
        uacUser.preUpdate();
        int updateResult = uacUserMapper.updateByPrimaryKeySelective(uacUser);
        if (updateResult < 1) {
            log.info("用户[{}]修改用户信息失败", uacUser.getId());
            return false;
        } else {
            log.info("用户[{}]修改用户信息成功", uacUser.getId());
        }
        // 更新token数据缓存


        // 更新用户角色和用户部门绑定信息
        return upsertUserRelateList(uacUserDto);
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean register(UacUserDto registerDto) {
        // 校验注册信息
        validateRegisterInfo(registerDto);
        // bean映射
        UacUser uacUser = BeanToBeanUtil.convertBean(registerDto, UacUser.class);
        // 完善注册信息
        uacUser.setId(UUID.randomUUID().toString());
        uacUser.setPassword(Md5Util.encrypt(registerDto.getLoginPwd()));
        uacUser.setUserSource(UacUserSourceEnum.REGISTER.getKey());
        //新注册账号是否需要审批？ DISABLE --> ENABLE (使用DISABLE的情况？)
        uacUser.setStatus(UacUserStatusEnum.DISABLE.getKey());
        uacUser.setCreateDate(new Date());
        uacUser.setLastLoginTime(new Date());
        uacUser.setCreateBy(getUserInfo().getId());
        // todo 发送激活邮件,后续可加
        SendEmailUtil.sendEmail(Maps.newHashMap(), registerDto.getEmail(), 1);
        log.info("注册用户. user={}", uacUser);
        uacUserMapper.insert(uacUser);
        //todo 注册用户可以绑定默认角色（基础用户），或不绑定
        return true;
    }

    @Override
    public List<String> getUserPerms(String userId) {
        // todo 这里
        List<String> menuPermList = uacMenuMapper.listMenuByUserId(userId)
                .stream()
                .filter(UacMenu -> StringUtils.isNotEmpty(UacMenu.getUrl()))
                .map(UacMenu::getUrl)
                .collect(Collectors.toList());
        return menuPermList;
    }

    @Override
    public int selectCountDuplicate(String loginName,String phone,String email){
        String term = mergeStr(loginName,phone,email);
        return this.uacUserMapper.selectCountDuplicate(term,loginName,phone,email);
    }

    /**
     * 更新用户最后登录时间
     * @param userId
     * @param request
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public void updateLastLoginTime(String userId, HttpServletRequest request) {
        if (StringUtils.isEmpty(userId) || request == null) {
            return;
        }
        UacUser uacUser = new UacUser();
        uacUser.setId(userId);
        uacUser.setLastLoginTime(new Date());
        uacUser.setLastLoginIp(IPUtil.getIpAddr(request));
        try {
            mapper.updateByPrimaryKeySelective(uacUser);
        } catch (Exception ignore) {}
    }

    /**
     * 更新/插入用户与office和role的关联关系
     *
     * @return boolean
     */
    private boolean upsertUserRelateList(UacUserDto uacUserDto) {
        if (uacUserDto == null || StringUtils.isEmpty(uacUserDto.getId())) {
            throw new BusinessException(ErrorCodeEnum.TPC10050005);
        }
        String userId = uacUserDto.getId();
        // 更新用户角色绑定信息
        // 1.删除旧的记录
        UacRoleUser select = new UacRoleUser();
        select.setUserId(userId);
        uacRoleUserMapper.delete(select);
        // 2.中间表插入新纪录
        List<String> roleIdList = uacUserDto.getRoleList().stream()
                .filter(Objects::nonNull).map(UacRole::getId).collect(Collectors.toList());
        boolean b = uacRoleUserService.saveList(userId, roleIdList);
        if (!b) {
            log.error("用户[{}]修改用户角色绑定失败---roleIdList:[{}]", userId, roleIdList);
        }

        // 更新用户部门绑定信息
        // 1.删除旧的记录
        UacUserOffice select2 = new UacUserOffice();
        select2.setUserId(userId);
        uacUserOfficeMapper.delete(select2);

        // 2.插入中间表
        List<String> officeIdList = uacUserDto.getOfficeList().stream().filter(Objects::nonNull)
                .map(UacOffice::getId)
                .collect(Collectors.toList());
        // 过滤不可用的机构数据
        List<String> filterOfficeList = uacOfficeService.filterDisableOffice(officeIdList);
        boolean b2 = uacUserOfficeService.saveList(userId, filterOfficeList);
        if (!b2) {
            log.error("用户[{}]修改用户角色部门失败---officeIdList:[{}]", userId, officeIdList);
        }
        // todo 修改缓存
        return true;
    }

    private void validateRegisterInfo(UacUserDto registerDto) {
        String mobileNo = registerDto.getPhone();
        Preconditions.checkArgument(!CollectionUtils.isEmpty(registerDto.getOfficeList()), "新建用户没有设置机构！");
        Preconditions.checkArgument(!CollectionUtils.isEmpty(registerDto.getRoleList()), "新建用户没有设置角色！");
        Preconditions.checkArgument(!checkDuplicateLoginName(registerDto), "用户名重复！");
        Preconditions.checkArgument(!checkDuplicateEmail(registerDto), "邮箱重复！");
        Preconditions.checkArgument(!checkDuplicatePhone(registerDto), "电话号码重复！");
        Preconditions.checkArgument(StringUtils.isNotEmpty(registerDto.getLoginName()), ErrorCodeEnum.UAC10011007.msg());
        Preconditions.checkArgument(StringUtils.isNotEmpty(registerDto.getEmail()), ErrorCodeEnum.UAC10011018.msg());
        Preconditions.checkArgument(StringUtils.isNotEmpty(mobileNo), "手机号不能为空");
        Preconditions.checkArgument(StringUtils.isNotEmpty(registerDto.getLoginPwd()), ErrorCodeEnum.UAC10011014.msg());
        Preconditions.checkArgument(StringUtils.isNotEmpty(registerDto.getConfirmPwd()), ErrorCodeEnum.UAC10011009.msg());
        Preconditions.checkArgument(checkPasswordStrength(registerDto.getLoginPwd()), "密码强度不够！");
        Preconditions.checkArgument(StringUtils.isNotEmpty(registerDto.getUserSource()), "验证类型错误");
        Preconditions.checkArgument(registerDto.getLoginPwd().equals(registerDto.getConfirmPwd()), "两次密码不一致");
    }

    public static void main(String[] args) {
        String loginPwd = "w1234567890@";

        System.out.println(loginPwd.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*?&<>=+-])[A-Za-z\\d$@$!%*?&<>=+-]{8,}$"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<GrantedAuthority> grantedAuthorities = null;
        UacUser user = this.getByLoginName(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        List<String> permissionList = uacMenuService.listMenuPerByUserId(user.getId());
        List<String> permissionNewList = new ArrayList<String>();
        for(String string : permissionList){
            if(!GyToolUtil.isNull(string)){
                permissionNewList.add(string);
            }
        }
        if (PublicUtil.isNotEmpty(permissionNewList)){
            grantedAuthorities = AuthorityUtils.createAuthorityList(permissionNewList.toArray(new String[permissionNewList.size()]));
        }
        UacUserDto uacUserDto = this.getUserInfoList(username);
        uacUserDto.setLoginPwd(null);
        uacUserDto.setPwdErrorCount(null);
        return new SecurityUser(user.getId(), username, user.getPassword(),
                user.getName(), user.getDelFlag(), user.getStatus(),uacUserDto, grantedAuthorities);
    }

    @Override
    public List<UacUser> getResponsibleUserByOfficeId(String officeId,UacUser uacUser){
        UacOffice office = uacOfficeService.getById(officeId);
        List<String> parentIds = office.getParentIds()==null||office.getParentIds().isEmpty()
                ?null:Arrays.asList(office.getParentIds().split(","));
        return uacUserMapper.getResponsibleUserByOfficeId(officeId, uacUser,parentIds);
    }

    @Override
    public boolean checkLoginUrl(String loginName,String roleType){
        if(roleType.equals("all")){
            return true;
        }
        UacUser user = uacUserMapper.getByLoginName(loginName);
        if (user == null) {
            return false;
        }
        //获取角色列表
        List<UacRole> ownRoleList = uacRoleMapper.listRolesByUserId(new String[]{user.getId()});
        for (UacRole role :ownRoleList){
            if(roleType.contains(role.getEnname()))
                return true;
        }
        return false;
    }
}
