package com.example.gbdpuserac.dto;

import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpuserac.core.BaseEntity;
import com.example.gbdpuserac.model.UacRole;
import com.example.gbdpuserac.model.UacUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * @author kwc
 * @date 2019/12/19
 */
@Data
@ApiModel("uacOffice中间类对象")
public class UacOfficeDto extends BaseEntity implements Serializable {
    private final static long serialVersionUID = 1L;
    /**
     * 父级编号
     */
    @NotBlank(message = "机构父Id不能为空")
    @ApiModelProperty(value = "父级Id", example = "1", required = true)
    private String parentId;
    /**
     * 所有父级编号
     */
    @ApiModelProperty(value = "所有父级编号", hidden = true)
    private String parentIds;
    /**
     * 名称
     */
    @NotBlank(message = "机构名不能为空", groups = CreateGroup.class)
    @Length(max = 20, message = "机构名不能超过20个字符", groups = CreateGroup.class)
    @ApiModelProperty(value = "机构名", example = "运维部")
    private String name;
    /**
     * 机构类型
     */
    //@NotBlank(message = "机构类型不能为空")
    @ApiModelProperty(value = "机构类型", example = "2")
    private String type;
    /**
     * 排序号
     */
    @ApiModelProperty(value = "机构排序", example = "999")
    private Integer sort;
    /**
     * 联系地址
     */
    @ApiModelProperty(value = "机构地址", example = "www.gbdp.com")
    private String address;
    /**
     * 邮政编码
     */
    @ApiModelProperty(value = "邮政编码", example = "100000")
    private String zipCode;
    /**
     * 电话
     */
    @ApiModelProperty(value = "电话", example = "010-10000000")
    private String phone;
    /**
     * 机构等级
     */
    @ApiModelProperty(value = "机构等级", hidden = true)
    private String grade;
    /**
     * 邮件地址
     */
    @ApiModelProperty(value = "邮箱", example = "company@gbdp.com")
    private String email;
    /**
     * 传真
     */
    @ApiModelProperty(value = "传真", example = "010-10000000")
    private String fax;
    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用 1-启用 0-禁用", example = "1")
    private String useAble;
    /**
     * 主负责人
     */
    @ApiModelProperty(value = "主负责人", example = "")
    private UacUser primaryPerson;
    /**
     * 副负责人
     */
    @ApiModelProperty(value = "副负责人", example = "")
    private UacUser deputyPerson;

    /**
     * 主负责人id
     */
    @ApiModelProperty(value = "主负责人id", hidden = true)
    private String primaryPersonId;
    /**
     * 副负责人id
     */
    @ApiModelProperty(value = "副负责人id", hidden = true)
    private String deputyPersonId;
    /**
     * office 关联角色
     */
    @ApiModelProperty(value = "office 关联角色", example = "[]")
    @JsonIgnore
    private List<UacRole> uacRoleList;

    /**
     * 父节点
     */
    @ApiModelProperty(value = "父节点Office", example = "")
    private UacOfficeDto parent;
}