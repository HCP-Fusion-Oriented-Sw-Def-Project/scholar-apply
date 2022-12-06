package com.example.gbdpuserac.service;


import com.example.gbdpbootcore.core.Service;
import com.example.gbdpbootcore.dto.ResetPasswordDto;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.model.UacUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
public interface UacUserService extends Service<UacUser> {
    /**
     * 根据登录名查询用户信息
     *
     * @param loginName the login name
     *
     * @return the uac user
     */
    UacUser getByLoginName(String loginName);

    /**
     * 根据用户ID查询用户信息.
     *
     * @param userCode the user id
     *
     * @return the uac user
     */
    UacUser getUacUserByUserCode(String userCode);


    /**
     * 更新用户信息
     *
     * @param uacUser the uac user
     * @return the int
     */
    boolean updateUser(UacUserDto uacUser);

    /**
     * 注册用户.
     *
     * @param registerDto the register dto
     */
    boolean register(UacUserDto registerDto);

    /**
     * 获得用户拥有的权限列表, 在首次验证用户对某个资源是否有权限时, 会调用此方法, 初始化用户权限
     *
     * @param userId the user id
     * @return the user perms
     */
    List<String> getUserPerms(String userId);

    /**
     * 获得项目下所有的资源权限列表, 用于程序启动时的初始化工作
     * String[0] = 资源
     * String[1] = 权限
     *
     * @return all perms
     */
    //List<Perm> getAllPerms();

    /**
     * 获取登录信息
     *
     * @return all UacUserDto
     */
    UacUserDto getUserInfoList(String username) throws UsernameNotFoundException;

    boolean updatePassword(ResetPasswordDto resetPasswordDto);

    boolean saveUser(UacUserDto uacUserDto);

    UacUserDto getUserDto(String id);

    PageResult getUserByRoleId(String roleId, UacUser uacUser, PageRequest pageRequest);

    boolean checkUserExist(String... userId);

    boolean checkDuplicateUser(UacUserDto uacUserDto);

    boolean checkDuplicateLoginName(UacUserDto uacUserDto);

    boolean checkDuplicatePhone(UacUserDto uacUserDto);

    boolean checkDuplicateEmail(UacUserDto uacUserDto);

    PageResult findUser(PageRequest pageRequest, UacUser uacUser);

    PageResult getUserByOfficeId(String officeId, UacUser uacUser, PageRequest pageRequest);

    boolean disableUser(String userId);

    int deleteUserById(String id);

    int deleteUserByIds(String ids);

    boolean checkPasswordStrength(String loginPwd);

    int selectCountDuplicate(String loginName,String phone,String email);

    void updateLastLoginTime(String userId, HttpServletRequest request);

    UserDetails loadUserByUsername(String username);

    List<UacUser> getResponsibleUserByOfficeId(String officeId,UacUser uacUser);

    boolean checkLoginUrl(String loginName,String roleType);
}
