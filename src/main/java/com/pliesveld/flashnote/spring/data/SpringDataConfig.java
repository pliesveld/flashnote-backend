package com.pliesveld.flashnote.spring.data;

import com.pliesveld.flashnote.repository.FlashCardRepository;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableJpaRepositories(basePackageClasses = FlashCardRepository.class)
@EnableSpringDataWebSupport
@ComponentScan(basePackageClasses = {FlashCardRepository.class})
public class SpringDataConfig {

}
