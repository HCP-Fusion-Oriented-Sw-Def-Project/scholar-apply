package com.example.gbdpuserac.service;



import com.example.gbdpbootcore.core.Service;
import com.example.gbdpuserac.model.UacUserToken;
import com.example.gbdpuserac.security.SecurityUser;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by CodeGenerator on 2018/09/18.
 */
public interface UacUserTokenService extends Service<UacUserToken> {
    void saveUserToken(OAuth2AccessToken token, final SecurityUser principal, HttpServletRequest request);
    /**
     * 获取token.
     *
     * @param accessToken the access token
     * @return the by access token
     */
    UacUserToken getByAccessToken(String accessToken);
    /**
     * 更新token.
     *
     * @param uacUserToken     the token dto
     */
    void updateUacUserToken(UacUserToken uacUserToken);
    /**
     * 刷新token.
     *
     * @param accessToken  the access token
     * @param refreshToken the refresh token
     * @param request      the request
     * @return the string
     */
    //String refreshToken(String accessToken, String refreshToken, HttpServletRequest request);
    /**
     * 更新token离线状态.
     *
     * @return the int
     */
    int batchUpdateTokenOffLine();
}
