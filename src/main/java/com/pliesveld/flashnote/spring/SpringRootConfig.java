package com.pliesveld.flashnote.spring;

import com.pliesveld.flashnote.audit.DateTimeService;
import com.pliesveld.flashnote.audit.DateTimeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
        "com.pliesveld.flashnote.service",
        "com.pliesveld.flashnote.repository",
        "com.pliesveld.flashnote.security"
})
public class SpringRootConfig {

    @Bean
    DateTimeService currentTimeDateTimeService() {
        return new DateTimeServiceImpl();
    }
}
