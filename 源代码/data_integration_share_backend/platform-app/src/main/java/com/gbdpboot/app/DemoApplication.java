package com.gbdpboot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.WebApplicationInitializer;

@SpringBootApplication(
        //scanBasePackages = {"com.example.gbdpbootcore", "com.example.gbdpuserac","com.gbdpboot.app"})
)
@ComponentScan(
        excludeFilters = {
                //@ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class)
        },
        basePackages = {"com.example.gbdpbootcore", "com.gbdpboot.app"})
public class DemoApplication extends SpringBootServletInitializer implements WebApplicationInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
