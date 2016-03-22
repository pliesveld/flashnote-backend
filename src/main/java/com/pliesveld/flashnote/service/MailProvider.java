package com.pliesveld.flashnote.service;


public interface MailProvider {
    boolean emailAccountRegistrationConfirmationLink(String email, String confirmURL);
    boolean emailAccountTemporaryPassword(String email, String temp_password);
    boolean emailAccountPasswordResetConfirmationLink(String email, String confirmURL);
}
