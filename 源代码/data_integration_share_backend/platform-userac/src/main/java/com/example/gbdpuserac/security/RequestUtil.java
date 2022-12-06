
package com.example.gbdpuserac.security;


import com.alibaba.fastjson.JSON;
import com.example.gbdpbootcore.constant.GlobalConstant;
import com.example.gbdpbootcore.dto.LoginAuthDto;
import com.example.gbdpbootcore.enums.ErrorCodeEnum;
import com.example.gbdpbootcore.exception.BusinessException;
import com.example.gbdpbootcore.util.PublicUtil;
import com.example.gbdpbootcore.util.ThreadLocalMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * The class Request util.
 *
 * @author gbdpcloud.net@gmail.com
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestUtil {
	static private final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

	/**
	 * Gets request.
	 *
	 * @return the request
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获得用户远程地址
	 *
	 * @param request the request
	 *
	 * @return the string
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader(GlobalConstant.X_REAL_IP);
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader(GlobalConstant.X_FORWARDED_FOR);
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader(GlobalConstant.PROXY_CLIENT_IP);
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader(GlobalConstant.WL_PROXY_CLIENT_IP);
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader(GlobalConstant.HTTP_CLIENT_IP);
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader(GlobalConstant.HTTP_X_FORWARDED_FOR);
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}
		if (StringUtils.isEmpty(ipAddress) || GlobalConstant.UNKNOWN.equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();
			if (GlobalConstant.LOCALHOST_IP.equals(ipAddress) || GlobalConstant.LOCALHOST_IP_16.equals(ipAddress)) {
				//根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					logger.error("获取IP地址, 出现异常={}", e.getMessage(), e);
				}
				assert inet != null;
				ipAddress = inet.getHostAddress();
			}
			logger.info("获取IP地址 ipAddress={}", ipAddress);
		}
		// 对于通过多个代理的情况, 第一个IP为客户端真实IP,多个IP按照','分割 //"***.***.***.***".length() = 15
		if (ipAddress != null && ipAddress.length() > GlobalConstant.MAX_IP_LENGTH) {
			if (ipAddress.indexOf(GlobalConstant.Symbol.COMMA) > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(GlobalConstant.Symbol.COMMA));
			}
		}
		return ipAddress;
	}

	/**
	 * Gets login user.
	 *
	 * @return the login user
	 */
	public static LoginAuthDto getLoginUser() {
		LoginAuthDto loginAuthDto = (LoginAuthDto) ThreadLocalMap.get(GlobalConstant.Sys.TOKEN_AUTH_DTO);
		if (PublicUtil.isEmpty(loginAuthDto)) {
			throw new BusinessException(ErrorCodeEnum.UAC10011039);
		}
		return loginAuthDto;

	}

	/**
	 * Gets auth header.
	 *
	 * @param request the request
	 *
	 * @return the auth header
	 */
	public static String getAuthHeader(HttpServletRequest request) {

		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		if (StringUtils.isEmpty(authHeader)) {
			throw new BusinessException(ErrorCodeEnum.UAC10011040);
		}
		return authHeader;
	}

	public static String[] extractAndDecodeHeader(String header) throws IOException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, "UTF-8");

		int delim = token.indexOf(GlobalConstant.Symbol.MH);

		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		return new String[]{token.substring(0, delim), token.substring(delim + 1)};
	}
	public static String sentHttpRequest(String url, Object request){
		CloseableHttpClient httpClient = null;
		CloseableHttpResponse response = null;
		String strEntity = "";
		try {
			httpClient = HttpClients.createDefault();
			final HttpPost httpPost = new HttpPost(url);

			final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(5000).setConnectionRequestTimeout(5000).setStaleConnectionCheckEnabled(true).build();
			httpPost.setConfig(requestConfig);

			final StringEntity urlEntity = new StringEntity(JSON.toJSONString(request), "utf-8");
			urlEntity.setContentEncoding("utf-8");
			urlEntity.setContentType("application/json");
			httpPost.setEntity(urlEntity);
			response = httpClient.execute(httpPost);
			final HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 200) {
				if (entity != null) {
					strEntity = EntityUtils.toString(entity, "utf-8");
				}
			}else {
				throw new BusinessException("http请求返回状态码错误" + String.valueOf(response.getStatusLine().getStatusCode()));
			}
		}catch(IOException e){
			throw new BusinessException("http请求超时");
		}finally{
			try {
				if(response != null){
					response.close();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			try {
				if(httpClient != null){
					httpClient.close();
				}
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return strEntity;
	}
}
