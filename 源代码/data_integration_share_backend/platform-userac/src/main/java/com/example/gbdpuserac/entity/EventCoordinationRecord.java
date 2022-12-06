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
import javax.persistence.Transient;

/**
 * (EventCoordinationRecord)实体类
 *
 * @author makejava
 * @since 2021-09-06 22:59:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(name = "event_coordination_record")
@ApiModel("event_coordination_record")
public class EventCoordinationRecord extends BaseEntity {
        private static final long serialVersionUID = 397690250311186829L;
         /**
         * 事件主键
         */
        @ApiModelProperty(value = "事件主键", dataType = "String")
                private String eventId;

        @ApiModelProperty(value = "事件主键", dataType = "String",hidden = true)
        private String deptId;
         /**
         * 状态（0开始处理、1处理结束）
         */
        @ApiModelProperty(value = "状态（0开始处理、1处理结束）", dataType = "Integer")
                private Integer processStatus;
        /**
         * 结果反馈
         */
        @ApiModelProperty(value = "结果反馈", dataType = "String")
                private String result;

        @Transient
        private Date issueTime;

        @Transient
        private String processPerson;

        @Transient
        private String coordinationDept;
        /**
         * 备注
         */
        @ApiModelProperty(value = "备注", dataType = "String")
        private String remark;
}
