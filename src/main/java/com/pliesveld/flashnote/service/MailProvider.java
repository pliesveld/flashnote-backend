package com.pliesveld.flashnote.service;

import org.springframework.validation.annotation.Validated;

@Validated
public interface MailProvider {
    boolean emailAccountRegistrationConfirmationLink(String email, String confirmURL);
    boolean emailAccountTemporaryPassword(String email, String temp_password);
    boolean emailAccountPasswordResetConfirmationLink(String email, String confirmURL);
}
