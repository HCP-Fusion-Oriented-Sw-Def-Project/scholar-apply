package com.example.gbdpuserac.filter;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @version 1.0
 * @ClassName CrosXssFilter
 * @Description //TODO
 * @Author lhf
 * @Date 2020-01-10 11:19
 **/
@Slf4j
//@WebFilter(filterName="crosXssFilter", urlPatterns="/*")
@WebFilter
@Order(-1)
public class CrosXssFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        //sql,xss过滤
        HttpServletRequest httpServletRequest=(HttpServletRequest)request;
        log.info("CrosXssFilter.......orignal url:{},ParameterMap:{}",httpServletRequest.getRequestURI(), JSONObject.toJSONString(httpServletRequest.getParameterMap()));
        XssHttpServletRequestWrapper xssHttpServletRequestWrapper=new XssHttpServletRequestWrapper(
                httpServletRequest);
        String method = httpServletRequest.getMethod();
        if ("OPTIONS".equalsIgnoreCase(method)) {
            response.getOutputStream().write("Success".getBytes("utf-8"));
        } else {
            chain.doFilter(xssHttpServletRequestWrapper, response);
        }
        log.info("CrosXssFilter..........doFilter url:{},ParameterMap:{}",xssHttpServletRequestWrapper.getRequestURI(), JSONObject.toJSONString(xssHttpServletRequestWrapper.getParameterMap()));
    }
    @Override
    public void destroy() {

    }

}
