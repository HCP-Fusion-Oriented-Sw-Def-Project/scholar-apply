package com.example.gbdpuserac.security;

import com.example.gbdpbootcore.httpResult.ResultCode;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpbootcore.publicToolUtil.ResponseGyUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.logstash.logback.encoder.org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认的退出成功处理器，如果设置了gbdpcloud.security.browser.signOutUrl，则跳到配置的地址上，
 * 如果没配置，则返回json格式的响应。
 *
 */
@Component("PcLogoutSuccessHandler")
public class PcLogoutSuccessHandler implements LogoutSuccessHandler {
	private final Logger logger = LoggerFactory.getLogger(PcLogoutSuccessHandler.class);

	private ObjectMapper objectMapper = new ObjectMapper();
	@Resource
	private TokenStore tokenStore;
	@Resource
	private RedisTemplate redisTemplate;

	/**
	 * On logout success.
	 *
	 * @param request        the request
	 * @param response       the response
	 * @param authentication the authentication
	 *
	 * @throws IOException the io exception
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException {
		logger.info("退出成功");
		response.setHeader("Access-Control-Allow-Origin", "*");
		String accessToken = request.getParameter("token");
		if(StringUtils.isNotBlank(accessToken)){
			OAuth2AccessToken oAuth2AccessToken  = tokenStore.readAccessToken(accessToken);
			if (!GyToolUtil.isNull(oAuth2AccessToken)) {
				System.out.println("----access_token是："+oAuth2AccessToken.getValue());
				tokenStore.removeAccessToken(oAuth2AccessToken);
				OAuth2RefreshToken oAuth2RefreshToken = oAuth2AccessToken.getRefreshToken();
				tokenStore.removeRefreshToken(oAuth2RefreshToken);
				tokenStore.removeAccessTokenUsingRefreshToken(oAuth2RefreshToken);
			}
		}
		ResponseGyUtil.ResponseData(response, ResultCode.SUCCESS.code(),"退出成功",objectMapper);
	}

}
