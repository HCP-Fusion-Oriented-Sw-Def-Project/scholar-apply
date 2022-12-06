package com.example.gbdpbootcore.autoconfigure;


import com.example.gbdpbootcore.config.FastJsonConfiguration;
import com.example.gbdpbootcore.config.PermitAllUrlProperties;
import com.example.gbdpbootcore.config.RedisConfig;
import com.example.gbdpbootcore.config.RedisConfiguration;
import com.example.gbdpbootcore.exception.GlobalExceptionHandler;
import com.example.gbdpbootcore.log.aspect.SystemLogAspect;
import com.example.gbdpbootcore.page.PageRequestArgumentResolver;
import com.example.gbdpbootcore.util.SpringContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @ClassName AutoConfiguration
 * @Description 自动配置类，把需要自动装配的类配置在这个类中实现自动配置
 * @Author kwc
 * @Date 2020-03-10 21:06
 **/
@Slf4j
@Configuration
public class AutoConfiguration {

    /**
     * controller方法日志切面打印
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SystemLogAspect.class)
    public SystemLogAspect systemLogAspect() {
        return new SystemLogAspect();
    }

    /**
     * 分页接口请求参数封装
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PageRequestArgumentResolver.class)
    public PageRequestArgumentResolver pageRequestArgumentResolver() {
        return new PageRequestArgumentResolver();
    }

    /**
     * 统一异常处理配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler exceptionHandler() {
        GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
        log.info("GlobalExceptionHandler [{}]", exceptionHandler);
        return exceptionHandler;
    }


    /**
     * Spring上下文工具配置
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(SpringContextHolder.class)
    public SpringContextHolder springContextHolder() {
        SpringContextHolder holder = new SpringContextHolder();
        log.info("SpringContextHolder [{}]", holder);
        return holder;
    }

    @Bean
    @ConditionalOnMissingBean(RedisConnectionFactory.class)
    public RedisConnectionFactory redisConnectionFactory(RedisProperties redisProperties) {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPassword(RedisPassword.of(redisProperties.getPassword()));
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        log.info("RedisConnectionFactory---redisProperties [{}]", redisProperties.getPort());
        return new LettuceConnectionFactory(redisStandaloneConfiguration);

    }

    @Bean
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        log.info("RestTemplate [{}]", restTemplate);
        return restTemplate;
    }

    /*@Bean
    @ConditionalOnMissingBean(RedisConfig.class)
    public RedisConfig redisConfig() {
        RedisConfig redisConfig = new RedisConfig();
        log.info("RedisConfig [{}]", redisConfig);
        return redisConfig;
    }

    @Bean
    @ConditionalOnMissingBean(RedisConfiguration.class)
    public RedisConfiguration redisConfiguration() {
        RedisConfiguration redisConfiguration = new RedisConfiguration();
        log.info("RedisConfiguration [{}]", redisConfiguration);
        return redisConfiguration;
    }*/


    @Bean
    @ConditionalOnMissingBean(FastJsonConfiguration.class)
    public FastJsonConfiguration fastJsonConfiguration() {
        FastJsonConfiguration fastJsonConfiguration = new FastJsonConfiguration();
        return fastJsonConfiguration;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        //先获取到converter列表
        List<HttpMessageConverter<?>> converters = builder.build().getMessageConverters();
        for(HttpMessageConverter<?> converter : converters){
            //因为我们只想要jsonConverter支持对text/html的解析
            if(converter instanceof MappingJackson2HttpMessageConverter){
                try{
                    //先将原先支持的MediaType列表拷出
                    List<MediaType> mediaTypeList = new ArrayList<>(converter.getSupportedMediaTypes());
                    //加入对text/html的支持
                    mediaTypeList.add(MediaType.TEXT_HTML);
                    mediaTypeList.add(MediaType.APPLICATION_JSON);
                    //将已经加入了text/html的MediaType支持列表设置为其支持的媒体类型列表
                    ((MappingJackson2HttpMessageConverter) converter).setSupportedMediaTypes(mediaTypeList);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return builder.build();
    }


    @Bean
    @ConditionalOnMissingBean(PermitAllUrlProperties.class)
    public PermitAllUrlProperties permitAllUrlProperties() {
        PermitAllUrlProperties permitAllUrlProperties = new PermitAllUrlProperties();
        return permitAllUrlProperties;
    }
}
