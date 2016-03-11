package com.pliesveld.flashnote.spring;

import com.pliesveld.flashnote.service.DateTimeService;
import com.pliesveld.flashnote.service.DateTimeServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@ComponentScan(basePackages = {
        "com.pliesveld.flashnote.service",
        "com.pliesveld.flashnote.repository",
        "com.pliesveld.flashnote.security"
})
public class SpringRootConfig {

    @Profile({Profiles.LOCAL})
    @Bean
    DateTimeService currentTimeDateTimeService()
    {
        return new DateTimeServiceImpl();
    }

}
