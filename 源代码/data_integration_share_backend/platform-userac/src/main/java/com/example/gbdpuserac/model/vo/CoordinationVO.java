package com.example.gbdpuserac.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class CoordinationVO {

    @ApiModelProperty(value = "事件主键", dataType = "String")
    private String eventId;

    @ApiModelProperty(value = "事件协同单位主键", dataType = "List<String>")
    private List<String> deptIds;

    @ApiModelProperty(value = "处理方式（0不处理并结束，1分发协同）", dataType = "Integer")
    private Integer processType;
}
