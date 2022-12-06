package com.example.gbdpuserac.model;

import com.example.gbdpuserac.core.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@Table(name = "uac_user_token")
public class UacUserToken extends BaseEntity {

    private static final long serialVersionUID = 3136723742371575367L;

    /**
     * 父ID
     */
    private String pid;

    /**
     * 登录名
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 姓名
     */
    @Column(name = "user_name")
    private String userName;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 登陆人Ip地址
     */
    @Column(name = "login_ip")
    private String loginIp;

    /**
     * 登录地址
     */
    @Column(name = "login_location")
    private String loginLocation;

    /**
     * 登录时间
     */
    @Column(name = "login_time")
    private Date loginTime;

    /**
     * 访问token
     */
    @Column(name = "access_token")
    private String accessToken;

    /**
     * 刷新token
     */
    @Column(name = "refresh_token")
    private String refreshToken;

    /**
     * token类型
     */
    @Column(name = "token_type")
    private String tokenType;

    /**
     * 访问token的生效时间(秒)
     */
    @Column(name = "access_token_validity")
    private Integer accessTokenValidity;

    /**
     * 刷新token的生效时间(秒)
     */
    @Column(name = "refresh_token_validity")
    private Integer refreshTokenValidity;

    /**
     * 0 在线 10已刷新 20 离线
     */
    private Integer status;

    /**
     * 组织ID
     */
    @Column(name = "office_id")
    private String officeId;

}