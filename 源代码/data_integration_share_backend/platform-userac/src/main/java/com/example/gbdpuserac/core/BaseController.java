package com.example.gbdpuserac.core;

import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.security.UacUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.annotation.Resource;

/**
 * @author kongweichang
 */
public class BaseController{

    protected Logger log = LoggerFactory.getLogger(BaseController.class);

    @Resource
    private TokenStore tokenStore;
    /**
     * 获取当前User对象
     */
    public UacUserDto getUserInfo() {
        return UacUserUtils.getUser(tokenStore);
    }

    @ModelAttribute
    public void common() {
        //执行公共的东西
        UacUserUtils.setUserInfo2Request(getUserInfo());
    }
}
