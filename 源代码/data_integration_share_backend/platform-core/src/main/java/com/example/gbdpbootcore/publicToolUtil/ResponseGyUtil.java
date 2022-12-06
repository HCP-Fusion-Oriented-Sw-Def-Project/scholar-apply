package com.example.gbdpbootcore.publicToolUtil;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.0
 * @ClassName ResponseGyUtil
 * @Description //TODO
 * @Author lhf
 * @Date 2020-03-21 14:15
 **/
@Slf4j
public class ResponseGyUtil {

    public static void ResponseData(HttpServletResponse response, int code, String message,ObjectMapper objectMapper) throws IOException {
        Map<String, Object> result = new HashMap<>(8);
        result.put("code", code);
        result.put("message", message);
        result.put("subCode", null);
        result.put("subMessage", null);
        result.put("data", null);
        String json = objectMapper.writeValueAsString(result);
        // 设置Response为200
        response.setStatus(HttpStatus.OK.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);
    }

    public static void ResponseData(HttpServletResponse response ,int code,String message) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        ServletOutputStream out = response.getOutputStream();
        try {
            Map<String, Object> result = new HashMap<>(8);
            result.put("code", code);
            result.put("message", message);
            result.put("subCode", null);
            result.put("subMessage", null);
            result.put("data", null);
            String json = JSON.toJSONString(result);
            out.write(json.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            log.info(e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
