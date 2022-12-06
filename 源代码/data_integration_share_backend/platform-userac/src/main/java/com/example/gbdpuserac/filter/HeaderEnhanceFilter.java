package com.example.gbdpuserac.filter;


import com.example.gbdpbootcore.config.PermitAllUrlProperties;
import com.example.gbdpbootcore.exception.BusinessException;
import com.example.gbdpbootcore.httpResult.ResultCode;
import com.example.gbdpbootcore.publicToolUtil.ResponseGyUtil;
import com.example.gbdpuserac.service.UacUserService;
import com.example.gbdpuserac.service.impl.UacUserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

@Component
//@WebFilter(filterName="headerEnhanceFilter", urlPatterns="*.*")
public class HeaderEnhanceFilter extends OncePerRequestFilter implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(HeaderEnhanceFilter.class);

    private static final String ANONYMOUS_USER_ID = "d4a65d04-a5a3-465c-8408-405971ac3346";
    private static RedisTemplate redisTemplate;


    @Resource
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, BusinessException {
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Headers",
                "DNT,client_id,client_secret,Accept-Encoding,Accept,Cookie,Authorization,Accept-Encoding,X-Mx-ReqToken,Keep-Alive,User-Agent,X-Requested-With,If-Modified-Since,Cache-Control,Content-Type,Authorization"
        );
        response.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");

        String authorization = ((HttpServletRequest) request).getHeader("Authorization");
        String requestURI = ((HttpServletRequest) request).getRequestURI();

        // todo 这里改回了原本的if判断式
        if(isPermitAllUrl(requestURI)) {
            if ("/datashare/login".equals(requestURI)){
                //登录进行校验验证码
                this.checkImageCode(request,response);
                this.checkLoginData(request,response);
            }
            filterChain.doFilter(request, response);
            return;
        }
        if (!StringUtils.isNotEmpty(authorization)) {
            LOGGER.info("Regard this request as anonymous request, so set anonymous user_id in the header.");
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private boolean isJwtBearerToken(String token) {
        return token.startsWith("Bearer") || token.startsWith("bearer");
    }



    private boolean isPermitAllUrl(String url) {
        PermitAllUrlProperties permitAllUrlProperties = new PermitAllUrlProperties();
        return permitAllUrlProperties.isPermitAllUrl(url);
    }

    private void checkLoginData(ServletRequest servletRequest,HttpServletResponse response) throws BusinessException, IOException {
        final String username = ((HttpServletRequest) servletRequest).getParameter("username");
        if (StringUtils.isEmpty(username))
        {
            ResponseGyUtil.ResponseData(response, ResultCode.ILLEGAL_PARAMETER.code(),"登录用户为空");
            throw new BusinessException("登录用户为空");
        }
        final String password =  servletRequest.getParameter("password");
        if (StringUtils.isEmpty(password))
        {
            ResponseGyUtil.ResponseData(response, ResultCode.ILLEGAL_PARAMETER.code(),"密码为空");
            throw new BusinessException("密码为空");
        }
        final String clientId =  ((HttpServletRequest) servletRequest).getHeader("client_id");
        if (StringUtils.isEmpty(clientId))
        {
            ResponseGyUtil.ResponseData(response, ResultCode.MISSING_PARAMETERS.code(),"clientid为空");
            throw new BusinessException("clientId为空");
        }
        final String clientSecret = ((HttpServletRequest) servletRequest).getHeader("client_secret");
        if (StringUtils.isEmpty(clientSecret))
        {
            ResponseGyUtil.ResponseData(response, ResultCode.MISSING_PARAMETERS.code(),"clientSecret为空");
            throw new BusinessException("clientSecret为空");
        }

//        final String type = ((HttpServletRequest) servletRequest).getHeader("type");
//        if (StringUtils.isEmpty(type))
//        {
//            ResponseGyUtil.ResponseData(response, ResultCode.MISSING_PARAMETERS.code(),"用户名或密码错误");
//            throw new BusinessException("用户名或密码错误");
//        }
    }
    /**
     *
     * Description:验证图片验证码是否正确
     * @param servletRequest
     * @date 2020年1月9日11:06:31
     */
    private void checkImageCode(ServletRequest servletRequest,HttpServletResponse response) throws BusinessException, IOException {


        final String randomStr= ((HttpServletRequest) servletRequest).getParameter("randomStr");
        String redisImageCode = (String) redisTemplate.opsForValue().get(randomStr);
        /*获取图片验证码与redis验证*/
        String imageCode = ((HttpServletRequest) servletRequest).getParameter("imageCode");
        if (StringUtils.isEmpty(imageCode))
        {
            ResponseGyUtil.ResponseData(response, ResultCode.MISSING_PARAMETERS.code(),"请录入验证码");
            throw new BusinessException("请录入验证码");
        }
        if (StringUtils.isEmpty(randomStr))
        {
            ResponseGyUtil.ResponseData(response, ResultCode.MISSING_PARAMETERS.code(),"请录入randomStr");
            throw new BusinessException("请录入randomStr");
        }
        /*redis的验证码不能为空*/
        if (StringUtils.isEmpty(redisImageCode))
        {
            ResponseGyUtil.ResponseData(response, ResultCode.ILLEGAL_PARAMETER.code(),"验证码超时，请刷新");
            throw new BusinessException("验证码超时，请刷新");
        }
        /*校验验证码*/
        if (!imageCode.equalsIgnoreCase(redisImageCode))
        {
            ResponseGyUtil.ResponseData(response, ResultCode.ILLEGAL_PARAMETER.code(),"验证码错误");
            throw new BusinessException("验证码错误");
        }
        redisTemplate.delete(randomStr);
    }
}
