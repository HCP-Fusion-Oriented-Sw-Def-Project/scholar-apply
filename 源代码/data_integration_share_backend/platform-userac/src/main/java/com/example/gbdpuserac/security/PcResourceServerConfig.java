package com.example.gbdpuserac.security;

import com.example.gbdpuserac.filter.HeaderEnhanceFilter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 资源服务器配置
 */
@Configuration
@EnableResourceServer
public class PcResourceServerConfig extends ResourceServerConfigurerAdapter {

	@Autowired
	private OAuth2WebSecurityExpressionHandler pcSecurityExpressionHandler;

	@Autowired
	private AccessDeniedHandler pcAccessDeniedHandler;

	@Autowired
	protected AuthenticationSuccessHandler pcAuthenticationSuccessHandler;

	@Autowired
	protected AuthenticationFailureHandler pcAuthenticationFailureHandler;

	@Autowired
    PcLogoutSuccessHandler pcLogoutSuccessHandler; //登出成功处理类

	@Autowired
    ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;//未登录处理类

	@Resource
	private DataSource dataSource;

	private static List<String> permitAllUrl = new ArrayList<>(Arrays.asList(
			"/oauth/**", "/**/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security",
			"/standard-information-platform/logout", "/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui",
			"/swagger-ui.html", "/standard-information-platform/login", "/swagger-resources/configuration/security",
			"/uac/file/**","/api/**","/codeController/**", "/standard/webPage/**"));

	@Value("${resources.paths:#{null}}")
	private String paths;

	/**
	 * 记住我功能的token存取器配置
	 *
	 * @return the persistent token repositRory
	 */
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
		//tokenRepository.setCreateTableOnStartup(true); // 第一次启动创建
		return tokenRepository;
	}

	/**
	 * Configure.
	 *
	 * @param http the http
	 *
	 * @throws Exception the exception
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		if (StringUtils.isNotEmpty(paths)) {
			String[] split = paths.split(",");
			for (String path : split) {
				String filePath = "/" + path + "/**";
				permitAllUrl.add(filePath);
			}
		}

		HeaderEnhanceFilter headerEnhanceFilter = new HeaderEnhanceFilter();
		http.cors().and()
				.addFilterBefore(headerEnhanceFilter, UsernamePasswordAuthenticationFilter.class)
				.formLogin()
				.loginProcessingUrl("/login").permitAll()
				.successHandler(pcAuthenticationSuccessHandler)
				.failureHandler(pcAuthenticationFailureHandler)
				.and()
				.logout().logoutUrl("/logout").permitAll()
				.clearAuthentication(true)
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
				.logoutSuccessHandler(pcLogoutSuccessHandler)
				.and()
				.authorizeRequests()
//				.antMatchers( "/oauth/**","/**/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/standard-information-platform/logout",
//						"/swagger-ui.html", "/webjars/**", "/swagger-resources/configuration/ui", "/swagger-ui.html", "/standard-information-platform/login",
//						"/swagger-resources/configuration/security","/uac/file/**","/api/**","/codeController/**", "/standard/webPage/**")
				.antMatchers(permitAllUrl.toArray(new String[0]))
				.permitAll()
				.anyRequest().authenticated()
				.and()
				.csrf().disable()
				.exceptionHandling().authenticationEntryPoint(resourceAuthExceptionEntryPoint)
				.accessDeniedHandler(pcAccessDeniedHandler);

	}


	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(null);
		resources.authenticationEntryPoint(resourceAuthExceptionEntryPoint);
		resources.expressionHandler(pcSecurityExpressionHandler);
	}
}