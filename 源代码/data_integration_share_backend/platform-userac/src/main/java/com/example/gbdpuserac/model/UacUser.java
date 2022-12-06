package com.example.gbdpuserac.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.gbdpbootcore.validation.CreateGroup;
import com.example.gbdpuserac.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import javax.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author gbdpcloud
 * @since 2019-12-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel("uacUser对象")
public class UacUser extends BaseEntity {

    private static final long serialVersionUID = 1L;
    /**
     * 登录名
     */
    @NotBlank(message = "登录名不能为空", groups = CreateGroup.class)
    @Length(max = 60, message = "登录名不能超过60个字符")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z][\\u4E00-\\u9FA5A-Za-z0-9\\*]*$", message = "登录名限制：最多60字符，包含文字、字母和数字，不能以数字开头", groups = CreateGroup.class)
    @ApiModelProperty(value = "登录名", example = "test_gbdp")
    private String loginName;
    /**
     * 登录密码
     */
    @JSONField(serialize = false)
    @JsonIgnore
    @ApiModelProperty(value = "密码", example = "123456")
    private String password;
    /**
     * 工号
     */
    @ApiModelProperty(value = "工号", example = "test_gbdp")
    @Length(max = 32, message = "工号不能超过32个字符")
    private String userCode;
    /**
     * 姓名
     */
    @ApiModelProperty(value = "用户名", example = "测试账号")
    @Length(max = 60, message = "用户名不能超过60个字符")
    @Pattern(regexp = "^[\\u4E00-\\u9FA5A-Za-z][\\u4E00-\\u9FA5A-Za-z0-9\\*]*$", message = "用户名限制：最多60字符，包含文字、字母和数字，不能以数字开头")
    private String name;
    /**
     * 手机号
     */
    @Pattern(regexp = "^[1][3,4,5,6,7,8,9][0-9]{9}$", message = "手机号格式有误")
    @Length(max=50, message="电话长度必须介于 1 和 50 之间")
    @ApiModelProperty(value = "手机号", example = "18800008888")
    private String phone;
    /**
     * 邮件地址
     */
    @Email(message="邮箱格式不正确")
    @ApiModelProperty(value = "邮箱", example = "test@gbdp.com")
    @Length(max=50, message="邮箱长度必须介于 1 和 50 之间")
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
    @Length(max=32, message="用户来源长度必须小于32")
    private String userSource;
    /**
     * 最后登录IP地址
     */
    @ApiModelProperty(value = "最后登录IP地址", hidden = true)
    @Length(max=50, message="电最后登录IP地址小于50")
    private String lastLoginIp;
    /**
     * 最后登录时间
     */
    @ApiModelProperty(value = "最后登录时间", hidden = true)
    private Date lastLoginTime;
    /**
     * 状态（是否可以登录）
     */
    @ApiModelProperty(value = "账号状态 ENABLE 、 DISABLE", example = "ENABLE")
    @Length(min=1, max=50, message="用户状态必须介于 1 和 50 之间")
    private String status;
    /**
     * 是否更改过密码
     */
    @ApiModelProperty(value = "是否更改过密码", hidden = true)
    private Integer isChangedPwd;
    /**
     * 连续输错密码次数（连续5次输错就冻结帐号）
     */
    @ApiModelProperty(value = "连续输错密码次数", hidden = true)
    private Integer pwdErrorCount;

    /**
     * 根据角色查询用户条件
     */
    @JSONField(serialize = false)
    @ApiModelProperty(value = "根据角色查询用户条件", example = "{}")
    private UacRole uacRole;

    /**
     * 根据机构查询用户条件(也作为用户的机构数据返回)
     */
    @ApiModelProperty(value = "根据机构查询用户条件(也作为用户所属的机构数据返回)", example = "{}")
    private UacOffice uacOffice;
}
