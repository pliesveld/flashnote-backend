package com.pliesveld.flashnote.spring.data;

import com.pliesveld.flashnote.repository.FlashCardRepository;
import com.pliesveld.flashnote.security.UsernameAuditorAware;
import com.pliesveld.flashnote.audit.AuditingDateTimeProvider;
import com.pliesveld.flashnote.audit.DateTimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@Configuration
@EnableJpaRepositories(basePackageClasses = FlashCardRepository.class)
@EnableSpringDataWebSupport
@ComponentScan(basePackageClasses = { FlashCardRepository.class } )
public class SpringDataConfig {

}
