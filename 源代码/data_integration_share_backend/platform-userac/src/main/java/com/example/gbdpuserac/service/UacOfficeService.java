package com.example.gbdpuserac.service;

import com.example.gbdpbootcore.core.Service;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpuserac.dto.UacOfficeDto;
import com.example.gbdpuserac.model.UacOffice;

import java.util.List;

/**
 * <p>
 * 机构表 服务类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
public interface UacOfficeService extends Service<UacOffice> {

    boolean saveOffice(UacOffice uacOfficeDto);

    boolean updateOffice(UacOffice uacOfficeDto);

    List<UacOffice> listAllOffice();

    UacOfficeDto getOfficeWithRole(String id);

    int deleteOfficeById(String id);

    int deleteOfficeByIds(String ids);

    /**
     * 树形结构返回office
     * @return List<UacMenu>
     */
    List<Object> officeTree();

    boolean checkDuplicateOffice(UacOffice uacOffice);

    boolean hasChild(String... id);

    List<Object> officeTreeByRoleId(String roleId);

    PageResult getOfficeByRoleId(String roleId, UacOffice uacOffice, PageRequest pageRequest);

    List<String> filterDisableOffice(List<String> officeIdList);
}
