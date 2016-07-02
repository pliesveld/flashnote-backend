package com.pliesveld.flashnote.spring;

import com.pliesveld.flashnote.service.FlashNoteService;
import com.pliesveld.flashnote.service.MailProvider;
import com.pliesveld.flashnote.spring.audit.SpringAuditConfig;
import com.pliesveld.flashnote.spring.data.SpringDataConfig;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.ActiveProfiles;


@Configuration
@ActiveProfiles(Profiles.INTEGRATION_TEST)
//@Import({SpringDataConfig.class, SpringAuditConfig.class, PersistenceContext.class})
@ComponentScan(basePackageClasses = FlashNoteService.class,
        excludeFilters = { @ComponentScan.Filter(Controller.class)})
public class SpringServiceTestConfig
{
    private static final Logger LOG = LogManager.getLogger();

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder()
    {
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public MailProvider mailProvider()
    {
        return new MailProvider() {
            @Override
            public boolean emailAccountRegistrationConfirmationLink(String email, String confirmURL) {
                LOG.debug("Emailing {} account verification token {}", email, confirmURL);
                return true;
            }

            @Override
            public boolean emailAccountTemporaryPassword(String email, String temp_password) {
                LOG.debug("Emailing {} temp password {}", email, temp_password);
                return true;
            }

            @Override
            public boolean emailAccountPasswordResetConfirmationLink(String email, String confirmURL) {
                LOG.debug("Emailing {} account password reset token {}", email, confirmURL);
                return true;
            }
        };
    }
}
