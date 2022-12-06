package com.example.gbdpuserac.service;

import com.example.gbdpbootcore.core.Service;
import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpuserac.dto.AverageProcessTimeDto;
import com.example.gbdpuserac.dto.ProportionDto;
import com.example.gbdpuserac.dto.StaticByTimeDto;
import com.example.gbdpuserac.entity.EventMessage;
import com.example.gbdpuserac.model.vo.StaticDeptVO;

import java.util.List;

/**
 * (EventMessage)表服务接口
 *
 * @author makejava
 * @since 2021-09-06 18:50:19
 */
public interface EventMessageService extends Service<EventMessage> {
    // 这里写自定义的service方法

    PageResult<EventMessage> pageMy(PageRequest page, EventMessage query);

    AverageProcessTimeDto averageProcessTime();

    List<ProportionDto> calProportion(String type);

    List<StaticByTimeDto> staticByHour();

    List<StaticByTimeDto> staticByDay();

    List<StaticByTimeDto> staticAll();

    List<StaticDeptVO> staticDept();
}

