package com.example.gbdpuserac.dto;

import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpuserac.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
@ApiModel("UacRole中间类对象")
public class UacRoleDto extends BaseEntity implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名不能为空", groups = CreateGroup.class)
    @Length(max = 20, message = "角色名不能超过20个字符", groups = CreateGroup.class)
    @ApiModelProperty(value = "角色名称", example = "测试用户")
    private String name;
    /**
     * 英文名称
     */
    @ApiModelProperty(value = "角色英文名称", example = "test_role")
    private String enname;
    /**
     * 角色类型
     */
    @NotBlank(message = "角色类型不能为空", groups = CreateGroup.class)
    @ApiModelProperty(value = "角色类型:assignment 任务分配 security-role 管理角色 user 普通角色",
            example = "user")
    private String roleType;
    /**
     * 数据
     */
    @NotBlank(message = "数据权限范围不能为空", groups = CreateGroup.class)
    @ApiModelProperty(value = "数据权限：1：所有数据；2：所在公司及以下数据；3：所在部门及以下数据；4：仅本人数据；5：访客数据", example = "4")
    private String dataScope;
    /**
     * 是否系统数据
     */
    @ApiModelProperty(value = "是否系统数据 1是||0否", example = "0")
    private String isSys;
    /**
     * 是否启用
     */
    @ApiModelProperty(value = "是否启用:1是||0否", example = "1")
    private String useAble;

    /**
     * 绑定的菜单信息
     */
    @ApiModelProperty(value = "绑定的菜单信息 树形结构", example = "[]")
    private List<Object> uacMenuList;

    /**
     * 绑定的机构信息
     */
    @ApiModelProperty(value = "绑定的机构信息 树形结构", example = "[]")
    private List<Object> uacOfficeList;
}