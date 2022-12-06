package com.example.gbdpuserac.config;

import com.example.gbdpuserac.filter.HeaderEnhanceFilter;
import com.example.gbdpuserac.security.PcAuthenticationFailureHandler;
import com.example.gbdpuserac.security.PcAuthenticationSuccessHandler;
import com.example.gbdpuserac.security.PcLogoutSuccessHandler;
import com.example.gbdpuserac.security.ResourceAuthExceptionEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

/**
 * The class Security core config.
 *
 * @author gbdpcloud
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {

    /**
     * 解决authenticationManager 无法注入
     */
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    private AccessDeniedHandler pcAccessDeniedHandler;
    @Autowired
    PcAuthenticationSuccessHandler pcAuthenticationSuccessHandler;		//认证成功处理类
    @Autowired
    PcAuthenticationFailureHandler pcAuthenticationFailureHandler; //认证失败处理类
    @Autowired
    PcLogoutSuccessHandler pcLogoutSuccessHandler; //登出成功处理类
    @Autowired
    ResourceAuthExceptionEntryPoint resourceAuthExceptionEntryPoint;//未登录处理类

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
