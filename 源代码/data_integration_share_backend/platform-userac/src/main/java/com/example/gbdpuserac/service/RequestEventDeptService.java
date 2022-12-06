package com.example.gbdpuserac.service;

import com.example.gbdpbootcore.core.Service;
import com.example.gbdpuserac.entity.RequestEventDept;

import java.util.List;

/**
 * (EventMessage)表服务接口
 *
 * @author makejava
 * @since 2021-09-06 18:50:19
 */
public interface RequestEventDeptService extends Service<RequestEventDept> {
    // 这里写自定义的service方法

    int deleteByEventId(String eventId);

}

