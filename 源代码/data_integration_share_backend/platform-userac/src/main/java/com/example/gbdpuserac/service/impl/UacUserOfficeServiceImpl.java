package com.example.gbdpuserac.service.impl;

import com.example.gbdpbootcore.enums.ErrorCodeEnum;
import com.example.gbdpbootcore.exception.BusinessException;
import com.example.gbdpbootcore.publicToolUtil.BeanToBeanUtil;
import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.mapper.UacUserOfficeMapper;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.model.UacUser;
import com.example.gbdpuserac.model.UacUserOffice;
import com.example.gbdpuserac.service.UacUserOfficeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * <p>
 * 用户机构中间表 服务实现类
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Service
@Transactional(readOnly = true)
public class UacUserOfficeServiceImpl extends BaseService<UacUserOffice> implements UacUserOfficeService {

    @Resource
    private UacUserOfficeMapper uacUserOfficeMapper;


    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean saveList(String uid, List<String> officeIdList) {
        List<UacUserOffice> collect = officeIdList.stream().map(officeId -> {
            UacUserOffice uacUserOffice = new UacUserOffice();
            uacUserOffice.setOfficeId(officeId);
            uacUserOffice.setUserId(uid);
            uacUserOffice.preSave();
            return uacUserOffice;
        }).collect(Collectors.toList());
        return saveBatch(collect) > 0;
    }

    /**
     * 批量更新bean
     * @param uacUserOffices 需要更新的bean
     * @return
     */
    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean updateList(List<UacUserOffice> uacUserOffices) {
        uacUserOffices.stream().filter(Objects::nonNull)
                .filter(uacUserOffice -> uacUserOffice.getId() != null)
                .forEach(
                        uacUserOffice -> uacUserOfficeMapper.updateByPrimaryKeySelective(uacUserOffice)
                );
        return true;
    }

    @Override
    public List<UacUserDto> listUserByOfficeId(String officeId) {
        if (StringUtils.isEmpty(officeId)) {
            throw new BusinessException(ErrorCodeEnum.TPC10050005);
        }

        List<UacUser> userList = uacUserOfficeMapper.listUserByOfficeId(officeId);
        List<UacUserDto> list = new ArrayList<>();
        BeanToBeanUtil.copyList(userList, list, UacUserDto.class);
        return list;
    }

    @Override
    @Transactional(readOnly = false, rollbackFor = Exception.class)
    public boolean updateUserByOfficeId(String officeId, List<String> userIds) {
        // 删除旧记录
        UacUserOffice uacUserOffice1 = new UacUserOffice();
        uacUserOffice1.setOfficeId(officeId);
        uacUserOfficeMapper.delete(uacUserOffice1);

        // 插入新记录
        List<UacUserOffice> newRecords = userIds.stream().filter(Objects::nonNull).map(
                userId -> {
                    UacUserOffice uacUserOffice = new UacUserOffice();
                    uacUserOffice.setUserId(userId);
                    uacUserOffice.setOfficeId(officeId);
                    uacUserOffice.preSave();
                    return uacUserOffice;
                }
        ).collect(Collectors.toList());
        int i = saveBatch(newRecords);
        return i > 0;
    }

}
