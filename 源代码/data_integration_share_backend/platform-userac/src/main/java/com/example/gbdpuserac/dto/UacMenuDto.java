package com.example.gbdpuserac.dto;

import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpbootcore.validation.UpdateGroup;
import com.example.gbdpuserac.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@ApiModel("uacMenu中间类对象")
public class UacMenuDto extends BaseEntity implements Serializable {
    private final static long serialVersionUID = 1L;

    /**
     * 父级Id
     */
    @Length(min = 1, max = 64, message = "父级Id长度有误")
    @NotBlank(message = "菜单父Id不能为空", groups = {CreateGroup.class, UpdateGroup.class})
    @ApiModelProperty(value = "父级Id", example = "1", required = true)
    private String parentId;

    /**
     * 角色名称
     */
    @NotBlank(message = "菜单名不能为空", groups = CreateGroup.class)
    @Length(max = 100, message = "菜单名不能超过100个字符", groups = CreateGroup.class)
    @ApiModelProperty(value = "菜单名称", example = "测试工具", required = true)
    private String name;

    /**
     * 菜单编码
     */
    @ApiModelProperty(value = "菜单编码（英文标记）", example = "test_menu", required = true)
    @Length(max = 100, message = "菜单编码长度有误")
    private String code;

    /**
     * 菜单URL
     */
    @Length(max = 150, message = "菜单url长度有误")
    @ApiModelProperty(value = "菜单路径（可显示菜单才有，非增删改查）", example = "/demo/test/menu")
    private String url;

    /**
     * 图标
     */
    @Length(max = 100, message = "图标长度有误")
    @ApiModelProperty(value = "菜单图标", example = "demo_icon")
    private String icon;

    /**
     * 是否在菜单中显示
     */
    @ApiModelProperty(value = "是否显示，1：显示，0：不显示", example = "0", required = true)
    @Length(min = 1, max = 1, message = "isShow长度有误")
    private String isShow;

    /**
     * 菜单类型   0：目录   1：菜单   2：按钮
     */
    @ApiModelProperty(value = "菜单类型 0：目录 1：菜单 2：按钮", example = "0")
    @Length(min = 1, max = 1, message = "菜单类型长度有误")
    private Integer type;

    /**
     * 权限标识
     */
    @ApiModelProperty(value = "菜单路径（操作资源菜单才有（增删改查））", example = "demo:test:edit")
    @Length(max = 200, message = "权限标识长度有误")
    private String permission;
}
