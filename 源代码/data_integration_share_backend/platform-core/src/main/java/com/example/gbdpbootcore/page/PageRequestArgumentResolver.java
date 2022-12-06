package com.example.gbdpbootcore.page;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 * @version 1.0
 * @ClassName PageRequestArgumentResolver
 * @Description PageRequest bean的封装
 * @Author kwc
 * @Date 2020-02-26 14:35
 **/
@Component
public class PageRequestArgumentResolver implements HandlerMethodArgumentResolver {


    /**
     * 判断Controller是否包含PageRequest参数
     *
     * @param parameter 参数
     * @return 是否过滤
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(PageRequest.class);
    }

    /**
     * 此方法可以将参数类型【pageNum=1&pageSize=1&orderRule="asc"】的转换成pageRequest bean
     *
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            return new PageRequest();
        }
        String orderBy = request.getParameter("orderBy");
        String pageNum = request.getParameter("pageNum");
        String pageSize = request.getParameter("pageSize");
        String orderRule = request.getParameter("orderRule");

        PageRequest pageRequest = new PageRequest();
        if (StringUtils.isNotBlank(pageNum)) {
            pageRequest.setPageNum(Integer.parseInt(pageNum));
        }
        if (StringUtils.isNotBlank(pageSize)) {
            pageRequest.setPageSize(Integer.parseInt(pageSize));
        }
        if (StringUtils.isNotBlank(orderBy)) {
            pageRequest.setOrderBy(orderBy);
        }
        if (StringUtils.isNotBlank(orderRule)) {
            pageRequest.setOrderRule(orderRule);
        }
        return pageRequest;
    }
}
