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
    AccountRegistrationToken deleteAccountRegistration(int id);

    @Transactional
    Student createStudent(String name, String email, String password) throws StudentCreateException;

    @Transactional
    AccountRegistrationToken createAccountRegistration(@Valid Student student) throws ResourceRepositoryException;

    @Transactional
    AccountPasswordResetToken findOrCreatePasswordResetToken(@Valid Student student);

    @Transactional
    Student processRegistrationConfirmation(String token);

    @Transactional
    Student processPasswordResetConfirmation(String token);

    void emailVerificationConfirmationURLtoAccountHolder(Student student, String confirmURL);

    void emailTemporaryPasswordToAccountHolder(String email, String temp_password);

    void emailPasswordResetToAccountHolder(String email, String confirmURL);
}