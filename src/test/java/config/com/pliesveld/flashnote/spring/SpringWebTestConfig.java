package com.pliesveld.flashnote.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan({"com.pliesveld.flashnote.controller"})
public class SpringWebTestConfig extends WebMvcConfigurationSupport {
    @Bean
    public MultipartResolver multipartResolver() {

        return new StandardServletMultipartResolver();

    }
}


