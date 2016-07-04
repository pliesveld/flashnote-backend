package com.pliesveld.flashnote.service;

import org.springframework.validation.annotation.Validated;

@Validated
public interface MailProvider {
    boolean emailAccountRegistrationConfirmationLink(final String email, final String confirmURL);

    boolean emailAccountTemporaryPassword(final String email, final String temp_password);

    boolean emailAccountPasswordResetConfirmationLink(final String email, final String confirmURL);
}
