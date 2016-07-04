package com.pliesveld.flashnote.spring;

import com.pliesveld.flashnote.audit.AuditingDateTimeProvider;
import com.pliesveld.flashnote.audit.DateTimeService;
import com.pliesveld.flashnote.spring.data.SpringDataConfig;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.ZonedDateTime;


@Configuration
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@Import({SpringSerializationTestConfig.class, SpringDataConfig.class, PersistenceContext.class, SpringEntityAuditTestConfig.class})
public class SpringEntityTestConfig {

    @Bean
    DateTimeService dateTimeService() {
        return new DateTimeService() {
            @Override
            public ZonedDateTime getCurrentDateAndTime() {
                return ZonedDateTime.now();
            }
        };
    }

    @ConditionalOnMissingBean(DateTimeProvider.class)
    @Bean
    protected DateTimeProvider dateTimeProvider(DateTimeService dateTimeService) {
        return new AuditingDateTimeProvider(dateTimeService);
    }

    @ConditionalOnMissingBean(AuditorAware.class)
    @Bean(name = "auditorAware")
    public AuditorAware<String> auditorAware() {

        return new TestUsernameAuditorAware();
    }

    private static class TestUsernameAuditorAware implements AuditorAware<String> {
        @Override
        public String getCurrentAuditor() {
            return "SYSTEM";
        }
    }
}

@Configuration
@ConditionalOnMissingBean(AuditorAware.class)
@EnableJpaAuditing(dateTimeProviderRef = "dateTimeProvider", auditorAwareRef = "auditorAware")
class SpringEntityAuditTestConfig {
}
