package com.pliesveld.flashnote.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class FlashnoteContainerApplication {

	public static void main(String[] args) {
        System.setProperty("spring.profiles.default", System.getProperty("spring.profiles.default", "local"));
        SpringApplication.run(FlashnoteContainerApplication.class, args);
	}
}
