package com.pliesveld.flashnote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@EnableScheduling
public class FlashnoteContainerApplication {

	public static void main(String[] args) {
//        System.setProperty("spring.profiles.default", System.getProperty("spring.profiles.default", "local"));
//        System.setProperty("spring.profiles.active", Profiles.LOCAL);
        SpringApplication.run(FlashnoteContainerApplication.class, args);
	}
}
