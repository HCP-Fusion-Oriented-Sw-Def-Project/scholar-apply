package com.example.gbdpuserac.model;

import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpuserac.core.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("uacRole对象")
public class UacRole extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名不能为空", groups = CreateGroup.class)
    @Length(max = 100, message = "角色名不能超过100个字符")
    @ApiModelProperty(value = "角色名称", example = "测试用户")
    private String name;
    /**
     * 英文名称
     */
    @ApiModelProperty(value = "角色英文名称", example = "test_role")
    @Length(max = 255, message = "角色名不能超过255个字符")
    private String enname;
    /**
     * 角色类型
     * assignment 任务分配
     * security-role 管理角色
     * user 普通角色
     */
    @ApiModelProperty(value = "角色类型:assignment 任务分配 security-role 管理角色 user 普通角色",
            example = "user")
    @NotBlank(message = "角色类型不能为空", groups = CreateGroup.class)
    @Length(max = 255, message = "角色类型不能超过255个字符")
    private String roleType;
    /**
     * 数据权限
     */
    @NotBlank(message = "数据权限范围不能为空", groups = CreateGroup.class)
    @ApiModelProperty(value = "数据权限：1：所有数据；2：所在公司及以下数据；3：所在部门及以下数据；4：仅本人数据；5：访客数据", example = "4")
    @Length(max = 1, message = "数据权限不能超过1个字符")
    private String dataScope;
    /**
     * 是否系统数据
     */
    @ApiModelProperty(value = "是否系统数据 1是||0否", example = "0")
    @Length(max = 64, message = "系统数据不能超过64个字符")
    private String isSys;
    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用:1是||0否", example = "1")
    @Length(max = 11, message = "是否启用不能超过11个字符")
    private String useAble;

    @ApiModelProperty(value = "角色绑定菜单")
    private List<String> uacActions;
}
