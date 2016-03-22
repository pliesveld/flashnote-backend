package com.pliesveld.flashnote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@ComponentScan
@EnableScheduling
public class FlashnoteContainerApplication {

	public static void main(String[] args) {
//        System.setProperty("spring.profiles.default", System.getProperty("spring.profiles.default", "local"));
//        System.setProperty("spring.profiles.active", Profiles.LOCAL);

        SpringApplication application = new SpringApplication(FlashnoteContainerApplication.class);
        //application.addListeners(new ApplicationPidFileWriter("./bin/app.pid"));
        application.run(args);
	}
}
