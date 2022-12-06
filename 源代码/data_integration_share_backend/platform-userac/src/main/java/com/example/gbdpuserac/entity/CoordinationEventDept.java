package com.example.gbdpuserac.entity;


import java.io.Serializable;
import java.util.Date;

import com.example.gbdpuserac.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Table;
/**
 * (CoordinationEventDept)实体类
 *
 * @author makejava
 * @since 2021-09-06 23:15:09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(name = "coordination_event_dept")
@ApiModel("coordination_event_dept")
public class CoordinationEventDept extends BaseEntity {
private static final long serialVersionUID = 149386315946914056L;
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

                private Integer status;

    public CoordinationEventDept() {
    }

    public CoordinationEventDept(String eventId, String deptId, Integer status) {
        this.eventId = eventId;
        this.deptId = deptId;
        this.status = status;
    }
}
