package com.example.gbdpbootcore.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 将请求封装成PageRequest对象
 *
 * @Author: kongweichang
 * @Date: 2019/12/23 10:40
 */
@Component
public class StringToPageRequestConverter implements Converter<String, PageRequest> {
    @Override
    public PageRequest convert(String source) {
        PageRequest pageRequest = new PageRequest();
        if (StringUtils.isEmpty(source)) {
            return pageRequest;
        }
        JSONObject jsonObject = JSON.parseObject(source);
        if (jsonObject != null) {
            Integer pageNum = jsonObject.getInteger("pageNum");
            Integer pageSize = jsonObject.getInteger("pageSize");
            String orderBy = jsonObject.getString("orderBy");

            if (pageNum != null) {
                pageRequest.setPageNum(pageNum);
            }
            if (pageSize != null) {
                pageRequest.setPageSize(pageSize);
            }
            if (StringUtils.isNotEmpty(orderBy)) {
                pageRequest.setOrderBy(orderBy);
            }
        }
        return pageRequest;
    }
}
