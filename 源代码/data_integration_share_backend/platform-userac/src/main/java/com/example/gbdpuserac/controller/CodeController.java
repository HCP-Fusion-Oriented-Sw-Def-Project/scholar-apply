package com.example.gbdpuserac.controller;

import com.example.gbdpbootcore.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.0
 * @ClassName CodeController
 * @Description //TODO
 * @Author lhf
 * @Date 2020-01-08 21:03
 **/
@Slf4j
@Controller
@RequestMapping("/codeController")
public class CodeController {
    @Autowired
    private  Producer producer;
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private ObjectMapper objectMapper;
    private static final String DEFAULT_CODE_KEY = "DEFAULT_CODE_KEY_";

    @RequestMapping("/getCode")
    public ModelAndView getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        // create the text for the image
        String capText = producer.createText();
        log.info("******************验证码是: " + capText + "******************");
        final String randomStr = request.getParameter("randomStr");
        if (StringUtils.isEmpty(randomStr))
        {
            throw new BusinessException("请录入randomStr");
        }
        redisTemplate.opsForValue().set(randomStr, capText, 60, TimeUnit.SECONDS);
        BufferedImage bi = producer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        // write the data out
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

}
