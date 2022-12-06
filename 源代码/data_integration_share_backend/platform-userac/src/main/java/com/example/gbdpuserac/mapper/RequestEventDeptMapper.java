package com.example.gbdpuserac.mapper;

import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpuserac.entity.EventMessage;
import com.example.gbdpuserac.entity.RequestEventDept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RequestEventDeptMapper extends IMapper<RequestEventDept> {
    //这里写自定义的mapper方法

}