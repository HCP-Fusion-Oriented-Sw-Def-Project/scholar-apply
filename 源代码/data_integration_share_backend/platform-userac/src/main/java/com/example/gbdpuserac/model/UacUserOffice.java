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
 * 用户机构中间表
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("UacUserOffice对象")
public class UacUserOffice extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 用户id
     */
    @NotBlank(message = "用户Id不能为空")
    @ApiModelProperty(value = "用户Id")
    @Length(max = 64, message = "用户Id不能超过64个字符")
    private String userId;
    /**
     * 部门id
     */
    @NotBlank(message = "机构Id不能为空")
    @ApiModelProperty(value = "机构Id")
    @Length(max = 64, message = "机构Id不能超过64个字符")
    private String officeId;

}
