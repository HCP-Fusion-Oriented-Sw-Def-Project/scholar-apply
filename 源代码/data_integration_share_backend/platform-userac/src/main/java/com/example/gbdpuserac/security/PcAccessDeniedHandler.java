package com.example.gbdpuserac.security;

import com.example.gbdpbootcore.httpResult.ResultCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The class Pc access denied handler.对页面请求的处理
 */
@Configuration
public class PcAccessDeniedHandler implements AccessDeniedHandler {
	private final Logger logger = LoggerFactory.getLogger(PcAccessDeniedHandler.class);

	@Resource
	private ObjectMapper objectMapper;

	/**
	 * Handle.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param e        the e
	 *
	 * @throws JsonProcessingException the json processing exception
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException {
		logger.info("处理权限异常. e={}", e);
		Map<String, Object> result = new HashMap<>(3);
		result.put("code", ResultCode.INSUFFICIENT_AUTHORIZATION.code());
		result.put("message", "无访问权限");
		String json = objectMapper.writeValueAsString(result);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(json);
	}
}