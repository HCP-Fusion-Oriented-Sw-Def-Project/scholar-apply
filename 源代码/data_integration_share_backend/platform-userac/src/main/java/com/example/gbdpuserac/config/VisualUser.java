package com.example.gbdpuserac.config;


import com.example.gbdpuserac.dto.UacUserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class VisualUser {

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static void setUserInfoFromRequest() {
        UacUserDto userDto = new UacUserDto();
        userDto.setId("nonLoginUser");
        getRequest().setAttribute("currentUser",userDto);
    }
}
