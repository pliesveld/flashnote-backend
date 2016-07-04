package com.pliesveld.flashnote.spring.audit;

import com.pliesveld.flashnote.audit.AuditingDateTimeProvider;
import com.pliesveld.flashnote.audit.DateTimeService;
import com.pliesveld.flashnote.repository.FlashCardRepository;
import com.pliesveld.flashnote.security.UsernameAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider", auditorAwareRef = "auditorAware")
@ComponentScan(basePackageClasses = {FlashCardRepository.class, AuditingDateTimeProvider.class})
public class SpringAuditConfig {

    @Bean
    protected DateTimeProvider dateTimeProvider(DateTimeService dateTimeService) {
        return new AuditingDateTimeProvider(dateTimeService);
    }

    @Bean(name = "auditorAware")
    protected AuditorAware<String> auditorAware() {
        return new UsernameAuditorAware();
    }
}
