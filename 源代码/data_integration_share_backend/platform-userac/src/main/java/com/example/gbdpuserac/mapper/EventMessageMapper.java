package com.example.gbdpuserac.mapper;

import com.example.gbdpbootcore.core.IMapper;
import com.example.gbdpuserac.dto.AverageProcessTimeDto;
import com.example.gbdpuserac.dto.ProportionDto;
import com.example.gbdpuserac.dto.StaticByTimeDto;
import com.example.gbdpuserac.entity.EventMessage;
import com.example.gbdpuserac.model.vo.StaticDeptVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * (EventMessage)表数据库访问层
 *
 * @author makejava
 * @since 2021-09-06 18:50:21
 */
@Mapper
public interface EventMessageMapper extends IMapper<EventMessage> {
    //这里写自定义的mapper方法

    List<EventMessage> listMy(EventMessage query);

    AverageProcessTimeDto averageProcessTime();

    List<ProportionDto> calProportion(@Param("type") String type);

    List<StaticByTimeDto> staticByHour();

    List<StaticByTimeDto> staticByDay();

    List<StaticByTimeDto> staticAll();

    List<StaticDeptVO> staticDept();
}


