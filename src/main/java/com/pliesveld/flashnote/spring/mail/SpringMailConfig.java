package com.pliesveld.flashnote.spring.mail;

import com.pliesveld.flashnote.service.AccountRegistrationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.SimpleMailMessage;

import java.sql.Date;
import java.time.Instant;

@Configuration
@ComponentScan(basePackageClasses = AccountRegistrationService.class)
@Import(MailSenderAutoConfiguration.class)
public class SpringMailConfig {

	@Bean
    @Qualifier("accountVerificationMessage")
	public SimpleMailMessage templateVerificationMessage() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(this.systemEmailAddress());
        message.setReplyTo(this.systemEmailAddress());
        message.setSentDate(Date.from(Instant.now()));
		message.setSubject("Account Registration Confirmation");
		message.setText("To complete your registration, visit ");
		return message;
	}

    @Bean
    @Qualifier("accountPasswordResetConfirmationMessage")
    public SimpleMailMessage templatePasswordResetConfirmationMessage()
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.systemEmailAddress());
        message.setReplyTo(this.systemEmailAddress());
        message.setSentDate(Date.from(Instant.now()));
        message.setSubject("Account Password Reset Confirmation");
        message.setText("Someone (hopefully you) has requested an account password reset.  To complete this process, visit ");
        return message;
    }

    @Bean
    @Qualifier("accountTemporaryPasswordMessage")
    public SimpleMailMessage templatePasswordResetMessage() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.systemEmailAddress());
        message.setReplyTo(this.systemEmailAddress());
        message.setSentDate(Date.from(Instant.now()));
        message.setSubject("Account Password Reset");
        message.setText("You have requested to reset your account password.  Once you login, you will have to change the temporary password.  Your temporary password is ");
        return message;
    }

    @Bean
    String systemEmailAddress()
    {
        return "flashnote.web@gmail.com";
    }
}