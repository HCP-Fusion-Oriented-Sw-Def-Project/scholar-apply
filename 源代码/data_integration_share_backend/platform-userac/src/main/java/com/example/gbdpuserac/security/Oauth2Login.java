package com.example.gbdpuserac.security;

import lombok.Data;

@Data
public class Oauth2Login{
    private int code;
    private String message;
    private String userid;
    private String access_token;
    private String token_type;
    private String refresh_token;
    private String scope;
    private String expires_in;
    private String bearer_type;
//    /**
//     * 用户菜单列表
//     */
//    private List<UacMenu> menuTree;
//
//    /**
//     * 用户角色列表
//     */
//    private List<UacRole> roleList;
//
//    /**
//     * 用户部门列表
//     */
//    private List<UacOffice> officeList;
}
