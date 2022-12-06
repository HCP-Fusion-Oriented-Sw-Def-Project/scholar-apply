package com.example.gbdpuserac.service.impl;

import com.example.gbdpbootcore.page.PageRequest;
import com.example.gbdpbootcore.page.PageResult;
import com.example.gbdpuserac.entity.RequestEventDept;
import com.example.gbdpuserac.mapper.EventMessageMapper;
import com.example.gbdpuserac.entity.EventMessage;
import com.example.gbdpuserac.core.BaseService;
import com.example.gbdpuserac.mapper.RequestEventDeptMapper;
import com.example.gbdpuserac.service.RequestEventDeptService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Condition;

import javax.annotation.Resource;
import java.util.List;

/**
 * (EventMessage)表服务实现类
 *
 * @author makejava
 * @since 2021-09-06 18:50:20
 */
@Transactional(readOnly = true)
@Service("requestEventDeptService")
public class RequestEventDeptServiceImpl extends BaseService<RequestEventDept> implements RequestEventDeptService {
    //这里写自定义service方法的实现

    @Resource
    private RequestEventDeptMapper requestEventDeptMapper;

    public int deleteByEventId(String eventId){
        Condition condition = new Condition(RequestEventDept.class);
        condition.createCriteria().andEqualTo("eventId",eventId);
        return requestEventDeptMapper.deleteByCondition(condition);
    }
}


