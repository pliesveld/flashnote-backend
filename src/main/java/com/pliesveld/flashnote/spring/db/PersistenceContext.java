package com.pliesveld.flashnote.spring.db;


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
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider")
@EnableJpaRepositories(basePackageClasses = com.pliesveld.flashnote.repository.FlashCardRepository.class)
@EnableTransactionManagement
@ComponentScan
public class PersistenceContext {

    @Bean
    DateTimeProvider dateTimeProvider(DateTimeService dateTimeService) {
        return new AuditingDateTimeProvider(dateTimeService);
    }

    @Bean
    AuditorAware<String> auditorAware() {
        return new UsernameAuditorAware();
    }


}
