package com.example.gbdpuserac.dto;

import com.alibaba.fastjson.JSON;
import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpuserac.core.BaseEntity;
import com.example.gbdpuserac.model.UacMenu;
import com.example.gbdpuserac.model.UacOffice;
import com.example.gbdpuserac.model.UacRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * User公用实体类
 * @author kongweichang
 */
@Data
@EqualsAndHashCode
@ToString
@Accessors(chain = true)
@ApiModel("uacUser中间类对象")
public class UacUserDto extends BaseEntity implements Serializable {
    private final static long serialVersionUID = 189996564L;

    /**
     * 登录名
     */
    @NotBlank(message = "登录名不能为空", groups = CreateGroup.class)
    @Length(max = 60, message = "登录名不能超过60个字符", groups = CreateGroup.class)
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z][\\u4E00-\\u9FA5A-Za-z0-9\\*]*$", message = "登录名限制：最多60字符，包含文字、字母和数字，不能以数字开头", groups = CreateGroup.class)
    @ApiModelProperty(value = "登录名", example = "test_gbdp")
    private String loginName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码:新建用户必填", example = "123456")
    private String loginPwd;

    /**
     * 确认密码
     */
    @ApiModelProperty(value = "确认密码:新建用户必填", example = "123456")
    private String confirmPwd;

    /**
     * 工号
     */
    @ApiModelProperty(value = "工号", example = "test_gbdp")
    private String userCode;

    /**
     * 姓名
     */
    @NotBlank(message = "用户名不能为空", groups = CreateGroup.class)
    @Length(max = 60, message = "用户名不能超过60个字符")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z][\\u4E00-\\u9FA5A-Za-z0-9\\*]*$", message = "用户名限制：最多60字符，包含文字、字母和数字，不能以数字开头")
    @ApiModelProperty(value = "用户名", example = "测试账号")
    private String name;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    @ApiModelProperty(value = "手机号", example = "18800008888")
    private String phone;

    /**
     * 邮件地址
     */
    @NotBlank(message = "联系邮箱不能为空")
    @Email(message = "邮箱格式不对")
    @ApiModelProperty(value = "邮箱", example = "test@gbdp.com")
    private String email;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像", example = "")
    private String photo;

    /**
     * 用户来源
     */
    @ApiModelProperty(value = "用户来源 REGISTER 注册 INSERT 管理员新建 ", example = "INSERT")
    private String userSource;

    /**
     * 状态（是否可以登录）
     */
    @ApiModelProperty(value = "账号状态 ENABLE 、 DISABLE", example = "ENABLE")
    private String status;

    /**
     * 是否更改过密码
     */
    @JsonIgnore
    private Integer isChangedPwd;

    /**
     * 连续输错密码次数（连续5次输错就冻结帐号）
     */
    @JsonIgnore
    private Integer pwdErrorCount;

    /**
     * 最后登录IP地址
     */
    @ApiModelProperty(value = "最后登录IP地址", hidden = true)
    private String lastLoginIp;

    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间", hidden = true)
    private Date lastLoginTime;


    /**
     * 用户权限列表
     */
    @ApiModelProperty(value = "用户权限列表", example = "[]")
    private List<UacMenu> menuList;

    /**
     * 用户角色列表
     */
    @ApiModelProperty(value = "用户角色列表:新建用户需要填UacRoleId", example = "[{\"id\":\"1\"},{\"id\":\"2\"}]")
    private List<UacRole> roleList;

    /**
     * 用户部门列表
     */
    @ApiModelProperty(value = "用户部门列表:新建用户需要填UacOfficeId", example = "[{\"id\":\"1\"},{\"id\":\"2\"}]")
    private List<UacOffice> officeList;
}