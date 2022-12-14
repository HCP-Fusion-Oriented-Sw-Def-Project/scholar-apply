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
 * ?????????????????????
 */
@Configuration
@EnableAuthorizationServer
@Slf4j
public class PcAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	//???????????????
	@Resource
	private AuthenticationManager authenticationManager;

	//UserDetailsService?????????????????????????????????????????????loadUserByUsername()???????????????username?????????????????????????????????????????????????????????????????????????????????????????????????????????????????????DaoAuthenticationProvider ???????????????????????????????????????????????????
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
				.allowFormAuthenticationForClients();//        ??????/oauth/token????????????????????????allowFormAuthenticationForClients?????????url??????client_id???client_secret?????????ClientCredentialsTokenEndpointFilter
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
				.userDetailsService(userDetailsService)//??????userService ?????????????????????????????????????????????????????????????????????
				//?????????????????????refresh token??????????????????,true:reuse;false:no reuse.
				.reuseRefreshTokens(false)
				.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST); //????????????get,post;

		//         ??????TokenServices??????
		DefaultTokenServices tokenServices = new DefaultTokenServices();
		tokenServices.setTokenStore(tokenStore());
		tokenServices.setSupportRefreshToken(true);
		tokenServices.setClientDetailsService(endpoints.getClientDetailsService());
		tokenServices.setTokenEnhancer(endpoints.getTokenEnhancer());
		tokenServices.setAccessTokenValiditySeconds( (int) TimeUnit.DAYS.toSeconds(1)); // 1???
	}

	/**
	 * ??????????????????????????????
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
