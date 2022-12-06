package com.example.gbdpuserac.entity;

import com.example.gbdpuserac.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Table;
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(name = "request_event_dept")
@ApiModel("request_event_dept")
public class RequestEventDept extends BaseEntity {
private static final long serialVersionUID = -28591495604279185L;
                                    /**
     * 事件主键
     */
    @ApiModelProperty(value = "事件主键", dataType = "String")
            private String eventId;
                                    /**
     * 单位主键
     */
    @ApiModelProperty(value = "单位主键", dataType = "String")
            private String deptId;

    public RequestEventDept(){

    }

    public RequestEventDept(String eventId, String deptId) {
        this.eventId = eventId;
        this.deptId = deptId;
    }
}
