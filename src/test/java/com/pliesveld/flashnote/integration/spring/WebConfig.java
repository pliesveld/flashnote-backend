package com.pliesveld.flashnote.integration.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@ComponentScan({ "com.pliesveld.flashnote.integration.controller"})
public class WebConfig extends WebMvcConfigurationSupport {
    @Bean
    public MultipartResolver multipartResolver() {

        return new StandardServletMultipartResolver();

    }
}


