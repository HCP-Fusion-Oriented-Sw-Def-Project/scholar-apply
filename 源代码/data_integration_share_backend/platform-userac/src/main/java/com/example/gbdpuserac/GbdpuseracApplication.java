package com.example.gbdpuserac;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@ServletComponentScan
@SpringBootApplication
// "com.example.gbdpbootcore", "com.example.gbdpuserac",
@ComponentScan(basePackages = {"com.example.gbdpbootcore", "com.example.gbdpuserac"})
public class GbdpuseracApplication {

    public static void main(String[] args) {
        SpringApplication.run(GbdpuseracApplication.class, args);
    }

}
