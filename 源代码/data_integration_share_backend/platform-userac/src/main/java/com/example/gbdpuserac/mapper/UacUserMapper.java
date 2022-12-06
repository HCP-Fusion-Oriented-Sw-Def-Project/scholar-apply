package com.example.gbdpuserac.mapper;

import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpuserac.model.UacUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Mapper
public interface UacUserMapper extends IMapper<UacUser> {
    /**
     * Find by login name uac user.
     *
     * @param loginName the login name
     *
     * @return the uac user
     */
    UacUser getByLoginName(String loginName);

    /**
     * Select user info by user id uac user.
     *
     * @param userCode the user id
     *
     * @return the uac user
     */
    UacUser getUacUserByUserCode(String userCode);

    List<UacUser> getUserByRoleId(@Param("roleId") String roleId, @Param("uacUser") UacUser uacUser);

    List<UacUser> findUser(UacUser uacUser);

    List<UacUser> getUserByOfficeId(@Param("officeId") String officeId, @Param("uacUser") UacUser uacUser);

    int selectCountDuplicate(@Param("term") String term,
                             @Param("loginName") String loginName,
                             @Param("phone") String phone,
                             @Param("email") String email);

    List<UacUser> getResponsibleUserByOfficeId(@Param("officeId") String officeId,
                                               @Param("uacUser") UacUser uacUser,
                                               @Param("parentIds") List<String> parentIds);

}
