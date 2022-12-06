package com.example.gbdpuserac.security;

import com.example.gbdpbootcore.httpResult.ResultCode;
import com.example.gbdpbootcore.publicToolUtil.ResponseGyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * APP环境下认证失败处理器
 */
@Component("pcAuthenticationFailureHandler")
public class PcAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Resource
    private ObjectMapper objectMapper;


    /**
     * On authentication failure.
     *
     * @param request   the request
     * @param response  the response
     * @param exception the exception
     * @throws IOException      the io exception
     * @throws ServletException the servlet exception
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        logger.info("登录失败");
        String message = "";
        int code = ResultCode.FAIL.code();
        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            message = "用户名或密码错误";
            code = ResultCode.ILLEGAL_PARAMETER.code();
        } else if (exception instanceof DisabledException) {
            message = "用户已被禁用";
            code = ResultCode.INSUFFICIENT_PERMISSIONS.code();
        } else if (exception instanceof LockedException) {
            message = "账户被锁定";
            code = ResultCode.INSUFFICIENT_PERMISSIONS.code();
        } else if (exception instanceof AccountExpiredException) {
            message = "账户过期";
            code = ResultCode.INSUFFICIENT_PERMISSIONS.code();
        } else if (exception instanceof CredentialsExpiredException) {
            message = "证书过期";
            code = ResultCode.TOKEN_EXPIRED.code();
        } else {
            message = "登录失败";
        }
        ResponseGyUtil.ResponseData(response, code, message, objectMapper);
    }

}
