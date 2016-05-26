package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AccountPasswordResetToken;
import com.pliesveld.flashnote.domain.AccountRegistrationToken;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.ResourceRepositoryException;
import com.pliesveld.flashnote.exception.StudentCreateException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
@Transactional(readOnly = true)
public interface AccountRegistrationService {

    long countAccountRegistration();
    long countStudent();
    long countStudentDetails();

    @Transactional
	void taskPurgeExpiredAccounts();

    @Transactional
    AccountRegistrationToken deleteAccountRegistration(final int id);

    @Transactional
    Student createStudent(final String name, final String email, final String password) throws StudentCreateException;

    @Transactional
    AccountRegistrationToken createAccountRegistration(@Valid final Student student) throws ResourceRepositoryException;

    @Transactional
    AccountPasswordResetToken findOrCreatePasswordResetToken(@Valid final Student student);

    @Transactional
    Student processRegistrationConfirmation(final String token);

    @Transactional
    Student processPasswordResetConfirmation(final String token);

    void emailVerificationConfirmationURLtoAccountHolder(final Student student, final String confirmURL);

    void emailTemporaryPasswordToAccountHolder(final String email, final String temp_password);

    void emailPasswordResetToAccountHolder(final String email, final String confirmURL);
}