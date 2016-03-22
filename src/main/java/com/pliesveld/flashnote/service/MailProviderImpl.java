package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.repository.RegistrationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

@Service("mailProvider")
@Controller
public class MailProviderImpl implements MailProvider {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private MailSender mailSender;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    @Qualifier("accountVerificationMessage")
    private SimpleMailMessage templateMessageVerificationConfirmation;

    @Autowired
    @Qualifier("accountPasswordResetConfirmationMessage")
    private SimpleMailMessage templateMessagePasswordResetConfirmation;

    @Autowired
    @Qualifier("accountTemporaryPasswordMessage")
    private SimpleMailMessage templateMessageTemporaryPassword;

    @Override
    public boolean emailAccountRegistrationConfirmationLink(String email, String confirmURL) {
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessageVerificationConfirmation);
        msg.setTo(email);
        msg.setText(msg.getText() + confirmURL);

        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {
            LOG.error("Email send failure", ex);
            return false;
        }


        return true;
    }

    @Override
    public boolean emailAccountPasswordResetConfirmationLink(String email, String confirmURL) {
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessagePasswordResetConfirmation);
        msg.setTo(email);
        msg.setText(msg.getText() + confirmURL);

        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {
            LOG.error("Email send failure", ex);
            return false;
        }

        return true;
    }


    @Override
    public boolean emailAccountTemporaryPassword(String email, String temp_password) {
        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessageTemporaryPassword);
        msg.setTo(email);
        msg.setText(msg.getText() + temp_password);

        try {
            this.mailSender.send(msg);
        } catch (MailException ex) {
            LOG.error("Email send failure", ex);
            return false;
        }

        return true;
    }

}
