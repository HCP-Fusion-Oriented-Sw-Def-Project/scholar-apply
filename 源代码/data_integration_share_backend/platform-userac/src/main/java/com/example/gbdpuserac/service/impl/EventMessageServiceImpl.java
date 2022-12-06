package com.example.gbdpuserac.service.impl;

import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.dto.AverageProcessTimeDto;
import com.example.gbdpuserac.dto.ProportionDto;
import com.example.gbdpuserac.dto.StaticByTimeDto;
import com.example.gbdpuserac.entity.EventMessage;
import com.example.gbdpuserac.mapper.EventMessageMapper;
import com.example.gbdpuserac.model.vo.StaticDeptVO;
import com.example.gbdpuserac.service.EventMessageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Transactional(readOnly = true)
@Service("eventMessageService")
public class EventMessageServiceImpl extends BaseService<EventMessage> implements EventMessageService {
    //这里写自定义service方法的实现

    @Resource
    private EventMessageMapper eventMessageMapper;

    public PageResult<EventMessage> pageMy(PageRequest page, EventMessage query){
        String order = String.format("%s %s", page.getOrderBy(), page.getOrderRule());
        PageHelper.startPage(page.getPageNum(), page.getPageSize(), order);
        List<EventMessage> select = eventMessageMapper.listMy(query);
        return PageResult.getPageResult(new PageInfo<>(select));
    }

    public AverageProcessTimeDto averageProcessTime(){
        return eventMessageMapper.averageProcessTime();
    }

    public List<ProportionDto> calProportion(String type){
        return eventMessageMapper.calProportion(type);
    }

    public List<StaticByTimeDto> staticByHour(){
        return eventMessageMapper.staticByHour();
    }

    public List<StaticByTimeDto> staticByDay(){
        return eventMessageMapper.staticByDay();
    }

    public List<StaticByTimeDto> staticAll(){
        return eventMessageMapper.staticAll();
    }

    public List<StaticDeptVO> staticDept(){
        return eventMessageMapper.staticDept();
    }
}
