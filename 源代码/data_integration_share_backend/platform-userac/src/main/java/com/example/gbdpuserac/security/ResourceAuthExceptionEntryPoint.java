package com.example.gbdpuserac.security;

import com.example.gbdpbootcore.httpResult.ResultCode;
import com.example.gbdpbootcore.publicToolUtil.ResponseGyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.provider.error.AbstractOAuth2SecurityExceptionHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version 1.0
 * @ClassName ResourceAuthExceptionEntryPoint
 * @Description //未登陆时，返回json
 * @Author lhf
 * @Date 2020-03-27 14:23
 **/

@Component
@AllArgsConstructor
@Slf4j
public class ResourceAuthExceptionEntryPoint extends OAuth2AuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Resource
    private ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        String message = "";
        if (authException != null) {
            message=authException.getMessage();
            log.error("======ResourceAuthExceptionEntryPoint-error:e:====", authException);
        }
        response.setStatus(HttpStatus.OK.value());
        ResponseGyUtil.ResponseData(response, ResultCode.ILLEGAL_TOKEN.code() ,message,objectMapper);
    }
}