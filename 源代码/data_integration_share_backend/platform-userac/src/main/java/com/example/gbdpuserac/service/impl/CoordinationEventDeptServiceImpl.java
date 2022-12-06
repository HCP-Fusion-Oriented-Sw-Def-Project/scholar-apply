package com.example.gbdpuserac.service.impl;

import com.example.gbdpuserac.mapper.CoordinationEventDeptMapper;
import com.example.gbdpuserac.entity.CoordinationEventDept;
import com.example.gbdpuserac.service.CoordinationEventDeptService;
import com.example.gbdpuserac.core.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (CoordinationEventDept)表服务实现类
 *
 * @author makejava
 * @since 2021-09-06 23:15:10
 */
@Transactional(readOnly = true)
@Service("coordinationEventDeptService")
public class CoordinationEventDeptServiceImpl extends BaseService<CoordinationEventDept> implements CoordinationEventDeptService {
        //这里写自定义service方法的实现
}


