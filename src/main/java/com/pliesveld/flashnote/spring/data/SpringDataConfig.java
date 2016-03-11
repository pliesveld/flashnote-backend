package com.pliesveld.flashnote.spring.data;

import com.pliesveld.flashnote.repository.FlashCardRepository;
import com.pliesveld.flashnote.security.UsernameAuditorAware;
import com.pliesveld.flashnote.service.AuditingDateTimeProvider;
import com.pliesveld.flashnote.service.DateTimeService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@EnableJpaRepositories(basePackageClasses = FlashCardRepository.class)
@ComponentScan(basePackageClasses = {FlashCardRepository.class,AuditingDateTimeProvider.class})
public class SpringDataConfig {
    @Bean
    DateTimeProvider dateTimeProvider(DateTimeService dateTimeService) {
        return new AuditingDateTimeProvider(dateTimeService);
    }

    @Bean
    AuditorAware<String> auditorAware() {
        return new UsernameAuditorAware();
    }
}
