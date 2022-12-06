package com.example.gbdpuserac.model;

import com.example.gbdpuserac.core.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 角色机构中间表
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("UacRoleOffice对象")
public class UacRoleOffice extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 角色id
     */
    @NotBlank(message = "角色Id不能为空")
    @ApiModelProperty(value = "角色id")
    @Length(max = 64, message = "角色id不能超过64个字符")
    private String roleId;
    /**
     * 部门id
     */
    @NotBlank(message = "机构Id不能为空")
    @ApiModelProperty(value = "部门id")
    @Length(max = 64, message = "部门id不能超过64个字符")
    private String officeId;

}
