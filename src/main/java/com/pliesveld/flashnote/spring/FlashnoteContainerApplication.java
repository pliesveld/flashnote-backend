package com.pliesveld.flashnote.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@PropertySource("classpath:application.properties")
public class FlashnoteContainerApplication {

	public static void main(String[] args) {
        System.setProperty("spring.profiles.default", System.getProperty("spring.profiles.default", "local"));
        System.setProperty("my.security.enabled", System.getProperty("my.security.enabled","true"));
        SpringApplication.run(FlashnoteContainerApplication.class, args);
	}
}
