package com.example.gbdpuserac.service.impl;

import com.example.gbdpuserac.entity.EventCoordinationRecord;
import com.example.gbdpuserac.service.EventCoordinationRecordService;
import com.example.gbdpuserac.core.BaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * (EventCoordinationRecord)表服务实现类
 *
 * @author makejava
 * @since 2021-09-06 23:06:15
 */
@Transactional(readOnly = true)
@Service("eventCoordinationRecordService")
public class EventCoordinationRecordServiceImpl extends BaseService<EventCoordinationRecord> implements EventCoordinationRecordService {
        //这里写自定义service方法的实现
}


