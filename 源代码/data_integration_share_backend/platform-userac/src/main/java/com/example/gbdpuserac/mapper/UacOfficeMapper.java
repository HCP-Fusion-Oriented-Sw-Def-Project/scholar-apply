package com.example.gbdpuserac.mapper;

import com.example.gbdpbootcore.annotation.DataScopePermission;
import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpuserac.model.UacOffice;
import com.example.gbdpuserac.model.UacRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 机构表 Mapper 接口
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Mapper
public interface UacOfficeMapper extends IMapper<UacOffice> {
    /**
     * Find office code listByPage by user id listByPage.
     *
     * @param userId the user id
     * @return the listByPage
     */
    List<UacOffice> listOfficeByUserId(String userId);

    /**
    *@Description 通过OfficeId查询其绑定的角色
    *@param officeId
    *@return List<UacRole>
    *@Author kwc
    *@date 2019/12/19
    */
    List<UacRole> listRoleByOfficeId(String officeId);

    @DataScopePermission
    List<UacOffice> getOfficeByRoleId(@Param("roleId") String roleId, @Param("uacOffice") UacOffice uacOffice);
}
