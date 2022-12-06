package com.example.gbdpbootcore.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 重置密码.
 */
@Data
public class ResetPasswordDto implements Serializable {
    private static final long serialVersionUID = 4762153981220090958L;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 原始密码
     */
    private String passwordOld;
    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    private String passwordNew;
    /**
     * 新密码
     */
    @NotBlank(message = "重新输入新密码不能为空")
    private String confirmPasswordNew;
    /**
     * 标记是否为找回密码
     */
    private boolean forgetPassword;
    /**
     * 重置密码输入账号对应邮箱
     */
    private String email;
    /**
     * 发送到邮箱的验证码
     */
    private String emailCode;

}
