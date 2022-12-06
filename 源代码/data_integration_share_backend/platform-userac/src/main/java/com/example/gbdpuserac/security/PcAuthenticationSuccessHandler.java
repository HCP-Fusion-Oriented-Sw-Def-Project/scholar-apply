package com.example.gbdpuserac.security;

import com.example.gbdpbootcore.exception.BusinessException;
import com.example.gbdpbootcore.httpResult.ResultCode;
import com.example.gbdpbootcore.publicToolUtil.GyToolUtil;
import com.example.gbdpbootcore.publicToolUtil.ResponseGyUtil;
import com.example.gbdpbootcore.util.PublicUtil;
import com.example.gbdpuserac.dto.UacUserDto;
import com.example.gbdpuserac.model.UacMenu;
import com.example.gbdpuserac.model.UacUser;
import com.example.gbdpuserac.service.UacMenuService;
import com.example.gbdpuserac.service.UacUserService;
import com.example.gbdpuserac.service.UacUserTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author gbdpcloud.net@gmail.com
 */
@Component("pcAuthenticationSuccessHandler")
public class PcAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	private final Logger logger = LoggerFactory.getLogger(PcAuthenticationSuccessHandler.class);
	@Resource
	private ObjectMapper objectMapper;
	@Resource
	private ClientDetailsService clientDetailsService;
	@Autowired
	private UacUserTokenService uacUserTokenService;
	@Autowired
	private UacUserService uacUserService;
	@Autowired
	private UacMenuService uacMenuService;

	@Resource
	private AuthorizationServerTokenServices authorizationServerTokenServices;
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private TokenStore tokenStore;
	@Autowired
	private RedisConnectionFactory connectionFactory;
	private AuthenticationKeyGenerator authenticationKeyGenerator=new
			DefaultAuthenticationKeyGenerator();


	private static final String BEARER_TOKEN_TYPE = "Basic ";

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws IOException, ServletException, BusinessException {

		logger.info("登录成功");

		String header = request.getHeader("Authorization");
		String clientId = request.getHeader("client_id");
		String clientSecret = request.getHeader("client_secret");
//		String roleType =  request.getHeader("type");
//		String username = request.getParameter("username");
//		if(!uacUserService.checkLoginUrl(username,roleType)){
//			ResponseGyUtil.ResponseData(response, ResultCode.ILLEGAL_PARAMETER.code(),"用户名或密码错误");
//			throw new BusinessException("用户名或密码错误");
//		}
		ClientDetails clientDetails = null;
		try {
			clientDetails = clientDetailsService.loadClientByClientId(clientId);
		}catch (ClientRegistrationException e){
			ResponseGyUtil.ResponseData(response, ResultCode.ILLEGAL_PARAMETER.code(),"clientId对应的配置信息错误:" + clientId);
			throw new BusinessException("clientId对应的配置信息错误:" + clientId);
		}
		if (clientDetails == null) {
			ResponseGyUtil.ResponseData(response, ResultCode.ILLEGAL_PARAMETER.code(),"clientId对应的配置信息不存在:" + clientId);
			throw new BusinessException("clientId对应的配置信息不存在:" + clientId);
		} else if (!clientSecret.equals(clientDetails.getClientSecret())) {
			ResponseGyUtil.ResponseData(response, ResultCode.ILLEGAL_PARAMETER.code(),"clientSecret对应的配置信息错误:" + clientId);
			throw new BusinessException("clientSecret不匹配:" + clientId);
		}
		TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");
		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
		OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
		SecurityUser principal = (SecurityUser) authentication.getPrincipal();
		uacUserTokenService.saveUserToken(token, principal, request);
		// 更新用户登录时间
		uacUserService.updateLastLoginTime(principal.getUserId(), request);
        /*
        登录成功以后获取用户信息将其放到redis中存贮
         */
		HttpServletResponse httpServletResponse=(HttpServletResponse)response;
		//通过在响应 header 中设置 ‘*’ 来允许来自所有域的跨域请求访问。
		httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");

		// this.updateRedisUserToken(principal.getUserId(), user);
		logger.info("用户【 {} 】记录登录日志", principal.getUsername());
		Oauth2Login loginInfo = new Oauth2Login();
		loginInfo.setCode(ResultCode.SUCCESS.code());
		loginInfo.setMessage("登录成功");
		loginInfo.setAccess_token(token.getValue());
		loginInfo.setRefresh_token(token.getRefreshToken().getValue());
		loginInfo.setExpires_in(String.valueOf(token.getExpiresIn()));
		loginInfo.setScope(token.getScope().toString());
		loginInfo.setToken_type(token.getTokenType());
		loginInfo.setUserid(principal.getUserId());
//		loginInfo.setMenuList(principal.getUacUserDto().getMenuList());
//		loginInfo.setOfficeList(principal.getUacUserDto().getOfficeList());
//		loginInfo.setRoleList(principal.getUacUserDto().getRoleList());
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write((objectMapper.writeValueAsString(loginInfo)));
	}

	public void updateRedisUserToken(String token, UacUserDto uacUserDto) {

		RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

		OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
		String key = authenticationKeyGenerator.extractKey(authentication);
		byte[] serializedKey =  serializationStrategy.serialize("auth_to_access:" + key);
		byte[] bytes = null;
		RedisConnection conn = connectionFactory.getConnection();
		try {
			bytes = conn.get(serializedKey);
		} finally {
			conn.close();
		}
		OAuth2AccessToken accessToken =serializationStrategy.deserialize(bytes,
				OAuth2AccessToken.class);

		// 修改用户数据
		SecurityUser principal = (SecurityUser) authentication.getUserAuthentication().getPrincipal();

		OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(token);
		SecurityUser securityUser  = (SecurityUser)tokenStore.readAuthentication(token).getUserAuthentication().getPrincipal();
		UacUserDto loginUser = securityUser.getUacUserDto();
		tokenStore.storeAccessToken(accessToken, authentication);
	}

	public void updateUserToken2(UacUserDto uacUserDto,String clientId){
		if (StringUtils.isEmpty(uacUserDto.getLoginName())){
			throw new BadCredentialsException("用户为空");
		}
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

		if (clientDetails == null) {
			throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
		}
		UacUser user = uacUserService.getByLoginName(uacUserDto.getLoginName());
		if (user == null) {
			throw new BadCredentialsException("用户名不存在或者密码错误");
		}
		UserDetails newSecurityUser = uacUserService.loadUserByUsername(uacUserDto.getLoginName());
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		List<String> permissionList = uacMenuService.listMenuPerByUserId(uacUserDto.getId());
		List<String> permissionNewList = new ArrayList<String>();
		for(String string : permissionList){
			if(!GyToolUtil.isNull(string)){
				permissionNewList.add(string);
			}
		}
		Collection<GrantedAuthority> grantedAuthorities = null;
		if (PublicUtil.isNotEmpty(permissionNewList)){
			grantedAuthorities = AuthorityUtils.createAuthorityList(permissionNewList.toArray(new String[permissionNewList.size()]));
		}
		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newSecurityUser, auth.getCredentials(),grantedAuthorities);
		TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");
		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, newAuth);
		SecurityContextHolder.getContext().setAuthentication(oAuth2Authentication);
//		SecurityContextHolder.getContext().setAuthentication(newAuth);

		RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();

		OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
		String key = authenticationKeyGenerator.extractKey(authentication);
		byte[] serializedKey =  serializationStrategy.serialize("auth_to_access:" + key);
		byte[] bytes = null;
		RedisConnection conn = connectionFactory.getConnection();
		try {
			bytes = conn.get(serializedKey);
		} finally {
			conn.close();
		}
		OAuth2AccessToken accessToken =serializationStrategy.deserialize(bytes,
				OAuth2AccessToken.class);
		tokenStore.storeAccessToken(accessToken, authentication);

	}

	public void refreshAuth(String username,String clientId){
		if (StringUtils.isEmpty(username)){
			throw new BadCredentialsException("用户名为空");
		}
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

		if (clientDetails == null) {
			throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
		}
		UacUser user = uacUserService.getByLoginName(username);
		if (user == null) {
			throw new BadCredentialsException("用户名不存在或者密码错误");
		}
		List<String> permissionList = uacMenuService.listMenuPerByUserId(user.getId());
		List<String> permissionNewList = new ArrayList<String>();
		for(String string : permissionList){
			if(!GyToolUtil.isNull(string)){
				permissionNewList.add(string);
			}
		}
		Collection<GrantedAuthority> grantedAuthorities = null;
		if (PublicUtil.isNotEmpty(permissionNewList)){
			grantedAuthorities = AuthorityUtils.createAuthorityList(permissionNewList.toArray(new String[permissionNewList.size()]));
		}
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");
		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(),grantedAuthorities);
		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, newAuth);
		SecurityContextHolder.getContext().setAuthentication(oAuth2Authentication);
		OAuth2Authentication authentication = (OAuth2Authentication)SecurityContextHolder.getContext().getAuthentication();
		RedisConnection conn = connectionFactory.getConnection();
		OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
		tokenStore.storeAccessToken(accessToken, authentication);
	}

}
