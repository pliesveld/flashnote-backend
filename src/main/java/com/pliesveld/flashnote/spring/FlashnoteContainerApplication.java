package com.pliesveld.flashnote.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class,ManagementWebSecurityAutoConfiguration.class})
@ComponentScan
public class FlashnoteContainerApplication {

	public static void main(String[] args) {
//        System.setProperty("spring.profiles.default", System.getProperty("spring.profiles.default", "local"));
        System.setProperty("spring.profiles.active",Profiles.LOCAL);
        SpringApplication.run(FlashnoteContainerApplication.class, args);
	}
}
