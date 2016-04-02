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
@Transactional
public interface AccountRegistrationService {

    @Transactional(readOnly = true)
    long countAccountRegistration();
    @Transactional(readOnly = true)
    long countStudent();
    @Transactional(readOnly = true)
    long countStudentDetails();

	void taskPurgeExpiredAccounts();

    AccountRegistrationToken deleteAccountRegistration(int id);

    Student createStudent(String name, String email, String password) throws StudentCreateException;

    AccountRegistrationToken createAccountRegistration(@Valid Student student) throws ResourceRepositoryException;

    AccountPasswordResetToken findOrCreatePasswordResetToken(@Valid Student student);

	Student processRegistrationConfirmation(String token);

    Student processPasswordResetConfirmation(String token);

    void emailVerificationConfirmationURLtoAccountHolder(Student student, String confirmURL);

    void emailTemporaryPasswordToAccountHolder(String email, String temp_password);

    void emailPasswordResetToAccountHolder(String email, String confirmURL);
}