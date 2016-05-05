package com.pliesveld.flashnote.spring;

import com.pliesveld.flashnote.service.FlashNoteService;
import com.pliesveld.flashnote.spring.audit.SpringAuditConfig;
import com.pliesveld.flashnote.spring.data.SpringDataConfig;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import com.pliesveld.flashnote.spring.mail.SpringMailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;


@Configuration
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@Import({SpringMailConfig.class, SpringDataConfig.class, SpringAuditConfig.class, PersistenceContext.class})
@ComponentScan(basePackageClasses = FlashNoteService.class)
@PropertySource("classpath:test-mail.properties" )
public class SpringMailServiceTestConfig
{

    @Bean
    @ConditionalOnMissingBean(PasswordEncoder.class)
    public PasswordEncoder passwordEncoder()
    {
        return org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance();
    }

    /*
    spring.mail.host=localhost
    spring.mail.port=2525
    spring.mail.username=test
    spring.mail.password=test
    */
    @Autowired
    Environment environment;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setHost(environment.getRequiredProperty("spring.mail.host"));
        sender.setPort(environment.getRequiredProperty("spring.mail.port", Short.class));
        sender.setUsername(environment.getRequiredProperty("spring.mail.username"));
        sender.setPassword(environment.getRequiredProperty("spring.mail.password"));

        return sender;
    }


}
