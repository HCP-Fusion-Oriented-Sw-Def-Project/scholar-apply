package com.example.gbdpuserac.entity;

import java.util.Date;
import java.io.Serializable;
import java.util.List;

import com.example.gbdpuserac.core.BaseEntity;
import com.example.gbdpuserac.model.vo.DeptVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * (EventMessage)实体类
 *
 * @author makejava
 * @since 2021-09-06 18:49:57
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(name = "event_message")
@ApiModel("event_message")
public class EventMessage extends BaseEntity {
    private static final long serialVersionUID = 523477765069625018L;
    private Long noKey;
    /**
     * 合同号
     */
    @ApiModelProperty(value = "合同号", dataType = "String")
    private String no;
    /**
     * 事件名称
     */
    @ApiModelProperty(value = "事件名称", dataType = "String")
    private String name;
    /**
     * 事件上报时间
     */
    @ApiModelProperty(value = "事件上报时间", dataType = "Date")
    private Date reportDate;
    /**
     * 事件影响产品或服务的名称
     */
    @ApiModelProperty(value = "事件影响产品或服务的名称", dataType = "String")
    private String serviceName;
    /**
     * 事件影响产品或服务的版本号
     */
    @ApiModelProperty(value = "事件影响产品或服务的版本号", dataType = "String")
    private String serviceVersion;
    /**
     * 事件影响产品或服务的供应商
     */
    @ApiModelProperty(value = "事件影响产品或服务的供应商", dataType = "String")
    private String serviceSupplier;
    /**
     * 事件影响产品或服务的设备位置
     */
    @ApiModelProperty(value = "事件影响产品或服务的设备位置", dataType = "String")
    private String servicePosition;
    /**
     * 事件影响产品或服务的ip
     */
    @ApiModelProperty(value = "事件影响产品或服务的ip", dataType = "String")
    private String serviceIp;
    /**
     * 信息泄露风险（0无，1一般，2严重）
     */
    @ApiModelProperty(value = "信息泄露风险（0无，1一般，2严重）", dataType = "String")
    private String disclosureRisk;
    /**
     * 数据完整性风险（0无，1一般，2严重）
     */
    @ApiModelProperty(value = "数据完整性风险（0无，1一般，2严重）", dataType = "String")
    private String dataIntegrityRisk;
    /**
     * 系统可用性风险（0无，1一般，2严重）
     */
    @ApiModelProperty(value = "系统可用性风险（0无，1一般，2严重）", dataType = "String")
    private String systemAvailabilityRisk;
    /**
     * 影响范围（0无，1一般，2严重，3非常严重）
     */
    @ApiModelProperty(value = "影响范围（0无，1一般，2严重，3非常严重）", dataType = "String")
    private String influenceScope;
    /**
     * 修复难度（0无，1一般，2严重，3非常严重）
     */
    @ApiModelProperty(value = "修复难度（0无，1一般，2严重，3非常严重）", dataType = "String")
    private String repairDifficulty;
    /**
     * 安全级别（0无危险，1低危，2中危，3高危，4超危）
     */
    @ApiModelProperty(value = "安全级别（0无危险，1低危，2中危，3高危，4超危）", dataType = "String")
    private String securityLevel;
    /**
     * 处理类型（0不处理，1自动处理，2人工处理）
     */
    @ApiModelProperty(value = "处理类型（0不处理，1自动处理，2人工处理）", dataType = "String")
    private String processType;
    /**
     * 分发类型（0自处理，1分发协同，2不分发协同）
     */
    @ApiModelProperty(value = "分发类型（0自处理，1分发协同，2不分发协同）", dataType = "String")
    private String distributionType;
    /**
     * 状态（0待提交、1待处理、2待协同、3协同中、-1已结束）
     */
    @ApiModelProperty(value = "状态（0待提交、1待处理、2待协同、3协同中、-1已结束）", dataType = "Integer")
    private Integer status;
    /**
     * 预留字段2
     */
    @ApiModelProperty(value = "预留字段2", dataType = "String")
    private String left2;
    /**
     * 预留字段3
     */
    @ApiModelProperty(value = "预留字段3", dataType = "String")
    private String left3;

    @ApiModelProperty(value = "提交时间", dataType = "String")
    private Date submitTime;

    @Transient
    private String reportDeptName;

    @Transient
    private String deptId;

    @Transient
    private String staticStatus;

    public void genNo(){
        StringBuilder sb = new StringBuilder();
        sb.append("NO");
        String noStr = String.valueOf(this.noKey);
        for(int i=0;i<8-noStr.length();i++){
            sb.append('0');
        }
        sb.append(noStr);
        this.no = sb.toString();
    }

    //List<DeptVO> requestDeptVOS;
    private String requestDeptVOS;
}
