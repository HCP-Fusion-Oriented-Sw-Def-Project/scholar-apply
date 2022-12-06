package com.example.gbdpuserac.config;

import com.example.gbdpuserac.config.RedisConfiguration2;
import com.example.gbdpuserac.security.PcLogoutSuccessHandler;
import com.example.gbdpuserac.security.RestClientDetailsServiceImpl;
import com.example.gbdpuserac.security.SecurityUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * 认证服务器配置
 */
@Configuration
@EnableAuthorizationServer
@Slf4j
public class PcAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	//认证管理器
	@Resource
	private AuthenticationManager authenticationManager;

	//UserDetailsService接口用于返回用户相关数据。它有loadUserByUsername()方法，根据username查询用户实体，可以实现该接口覆盖该方法，实现自定义获取用户过程。该接口实现类被DaoAuthenticationProvider 类使用，用于认证过程中载入用户信息
	private UserDetailsService userDetailsService;

	private RestClientDetailsServiceImpl restClientDetailsService;

	private RedisConnectionFactory redisConnectionFactory;

	private RedisConfiguration2 redisConfiguration2;

	@Autowired
	public PcAuthorizationServerConfig( RedisConnectionFactory redisConnectionFactory,
									   @Qualifier("uacUserDetailsServiceImpl") UserDetailsService userDetailsService,
									   RestClientDetailsServiceImpl restClientDetailsService,
									   RedisConfiguration2 redisConfiguration2) {
		this.redisConnectionFactory = redisConnectionFactory;
		this.userDetailsService = userDetailsService;
		this.restClientDetailsService = restClientDetailsService;
		this.redisConfiguration2 = redisConfiguration2;
	}
	
	
	/**
	 * Configure.
	 *
	 * @param security the security
	 *
	 * @throws Exception the exception
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security
				.passwordEncoder(new BCryptPasswordEncoder())
				.allowFormAuthenticationForClients();//        请求/oauth/token的，如果配置支持allowFormAuthenticationForClients的，且url中有client_id和client_secret的会走ClientCredentialsTokenEndpointFilter
	}

	@Bean
	@ConditionalOnMissingBean(RedisConnectionFactory.class)
	public RedisConnectionFactory redisConnectionFactory(RedisConfiguration2 redisConfiguration2) {
		RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
		redisStandaloneConfiguration.setDatabase(redisConfiguration2.getDatabase());
		redisStandaloneConfiguration.setHostName(redisConfiguration2.getHost());
		redisStandaloneConfiguration.setPassword(RedisPassword.of(redisConfiguration2.getPassword()));
		redisStandaloneConfiguration.setPort(redisConfiguration2.getPort());
		log.info("RedisConnectionFactory---redisProperties [{}]", redisConfiguration2.getPort());
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	@Bean
	public TokenStore tokenStore() {
		return new RedisTokenStore(redisConnectionFactory);
	}



	/**
	 * Configure.
	 *
	 * @param clients the clients
	 *
	 * @throws Exception the exception
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(restClientDetailsService);
	}

	/**
	 * Configure.
	 *
	 * @param endpoints the endpoints
	 *
	 * @throws Exception the exception
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.tokenStore(tokenStore())
				.tokenEnhancer(tokenEnhancer())
				.authenticationManager(authenticationManager)
				.userDetailsService(userDetailsService)//配置userService 这样每次认证的时候会去检验用户是否锁定，有效等
				//该字段设置设置refresh token是否重复使用,true:reuse;false:no reuse.
				.reuseRefreshTokens(false)
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST); //设置允许get,post;

		//         配置TokenServices参数
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(tokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
		tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
		tokenServices.setAccessTokenValiditySeconds( (int) TimeUnit.DAYS.toSeconds(1)); // 1天
	}

	/**
	 * 退出时的处理策略配置
	 *
	 * @return logout success handler
	 */
	@Bean
	public LogoutSuccessHandler logoutSuccessHandler() {
		return new PcLogoutSuccessHandler();
	}

	/**
	 *
	 *
	 * @return logout success handler
	 */

	@Bean
	public TokenEnhancer tokenEnhancer() {
		return (accessToken, authentication) -> {
			final Map<String, Object> additionalInfo = new HashMap<>(4);
			SecurityUser securityUser = (SecurityUser) authentication.getUserAuthentication().getPrincipal();
			additionalInfo.put("token-uacUser",securityUser.getUacUserDto());
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
			return accessToken;
		};
	}
}
