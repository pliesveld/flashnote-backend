package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.ResourceLimitException;
import com.pliesveld.flashnote.exception.ResourceRepositoryException;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.repository.PasswordResetRepository;
import com.pliesveld.flashnote.repository.RegistrationRepository;
import com.pliesveld.flashnote.repository.StudentDetailsRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class AccountRegistrationServiceImpl implements AccountRegistrationService {
    private static final Logger LOG = LogManager.getLogger();
    private static final Logger taskLogger =
            LogManager.getLogger(LOG.getName() + ".[task]");


    @Autowired
    MailProvider mailProvider;

	@Autowired
	RegistrationRepository registrationRepository;

    @Autowired
    PasswordResetRepository passwordResetRepository;
	
	@Autowired
	StudentRepository studentRepository;

    @Autowired
    StudentDetailsRepository studentDetailsRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public AccountRegistrationToken createAccountRegistration(@Valid Student student) throws ResourceRepositoryException {
        Integer student_id = student.getId();

        if(!studentRepository.exists(student.getId()))
        {
            throw new StudentNotFoundException(student_id);
        }

        String token = UUID.randomUUID().toString().substring(0,8).toUpperCase();

        AccountRegistrationToken registration = new AccountRegistrationToken(student,token);
        registrationRepository.save(registration);
        return registration;
    }

    @Override
    public AccountPasswordResetToken findOrCreatePasswordResetToken(@Valid Student student) {
        Integer student_id = student.getId();

        if(!studentRepository.exists(student_id))
        {
            throw new StudentNotFoundException(student_id);
        }

        AccountPasswordResetToken resetToken = passwordResetRepository.findOne(student.getId());

        String token = UUID.randomUUID().toString().substring(0,8).toUpperCase();

        if(resetToken == null)
        {
            resetToken = new AccountPasswordResetToken(student, token);
            passwordResetRepository.save(resetToken);
        } else {
            resetToken.getEmailSentOn();
            resetToken.getToken();
        }

        return resetToken;
    }

    @Override
    public AccountRegistrationToken deleteAccountRegistration(int id) {
        AccountRegistrationToken registration = registrationRepository.findOne(id);
        if(registration != null)
        {
            registrationRepository.delete(registration);
        }
        return registration;
    }

    @Override
    public Student createStudent(String name, String email, String password) throws StudentCreateException {

        Student other = studentRepository.findOneByEmail(email);

        if(other != null)
            throw new StudentCreateException(email);

        StudentDetails studentDetails = new StudentDetails();
        studentDetails.setName(name);

        Student student = new Student();
        student.setEmail(email);
        //student.setPassword(password);
        student.setPassword(passwordEncoder.encode(password));
        student.setRole(StudentRole.ROLE_ACCOUNT);

        student.setStudentDetails(studentDetails);
        studentDetails.setStudent(student);

        try {
            student = studentRepository.save(student);
            studentDetails = studentDetailsRepository.save(studentDetails);
        } catch(DataAccessException cdae) {
            throw new StudentCreateException("Could not create account");
        }

        return student;
    }



	@Override
	public Student processRegistrationConfirmation(String token) {
		AccountRegistrationToken registration = registrationRepository.findByToken(token);
        Student student = null;
		if(registration == null || (student = registration.getStudent()) == null)
			return null;

		if(student.getRole() == StudentRole.ROLE_ACCOUNT)
		{
			LOG.info("User registrated: {}",student.getEmail());
			student.setRole(StudentRole.ROLE_USER);
			registrationRepository.delete(registration);
			return studentRepository.save(student);
		} else {
			LOG.warn("User registration: User {} has role {} != ROLE_ACCOUNT",student.getEmail(),student.getRole());
			registrationRepository.delete(registration);
			return student;
		}
	}

    @Override
    public Student processPasswordResetConfirmation(String token) {
        AccountPasswordResetToken resetToken = passwordResetRepository.findByToken(token);
        Student student = null;

        if(resetToken == null || (student = resetToken.getStudent()) == null)
            return null;

        LOG.info("Setting temporary password for {}", student.getEmail());

        String temp_password = "";

        for(int i = 0; i < 8;i++)
        {
            String source = UUID.randomUUID().toString();
            int rnd_idx = ThreadLocalRandom.current().nextInt(source.length());
            char c = source.charAt(rnd_idx);
            temp_password += c;
        }

        temp_password = temp_password.toUpperCase();

        student.setPassword(temp_password);
        student.setTemporaryPassword(true);
        student.setLastPasswordResetDate(Instant.now());
        studentRepository.save(student);

        passwordResetRepository.delete(resetToken);

        return student;
    }

    /* 5am every day */
	@Scheduled(cron = "0 0 5 * * ?")
	@Override
	public void taskPurgeExpiredAccounts() {
		taskLogger.info("Checking accounts to be purged.");

		
		List<AccountRegistrationToken> list_expired =
				registrationRepository.findAllByExpirationLessThan(Instant.now())
				.collect(Collectors.toList());
		
		List<Student> list_student =
		list_expired.stream().map(AccountRegistrationToken::getStudent)
			.filter((student) -> student.getRole() == StudentRole.ROLE_ACCOUNT)
			.collect(Collectors.toList());

		taskLogger.info("Purging {} registration tokens",list_expired.size());
		registrationRepository.delete(list_expired);

		StringBuilder sb = new StringBuilder();
		sb.append("Purging accounts (id, email): ");
		list_student.forEach((student) -> {
			sb.append("(")
				.append(student.getId()).append(',')
				.append(student.getEmail()).append(") ");
			});
				
		taskLogger.info(sb.toString());
		studentRepository.delete(list_student);
		
	}

    @Override
    public long countAccountRegistration() {
        return registrationRepository.count();
    }

    @Override
    public long countStudent() {
        return studentRepository.count();
    }

    @Override
    public long countStudentDetails() {
        return studentDetailsRepository.count();
    }

    @Override
    public void emailVerificationConfirmationURLtoAccountHolder(Student student, String confirmURL) {
        AccountRegistrationToken registration = registrationRepository.findOne(student.getId());

        if(registration == null)
        {
            throw new StudentNotFoundException("No registration found for student id: " + student.getId());
        }


        Instant last_email = registration.getEmailSentOn();
        Instant email_threshold = Instant.now().plus(3L, ChronoUnit.HOURS);

        if(last_email != null)
        {
            if(last_email.isBefore(email_threshold))
            {
                LOG.info("refusing to send email before {}", email_threshold);
                throw new ResourceLimitException("Cannot resend email until " + email_threshold);
            }
        }

        if(mailProvider.emailAccountRegistrationConfirmationLink(student.getEmail(), confirmURL))
        {
            registration.setEmailSentOn(Instant.now());
        }
    }

    @Override
    public void emailTemporaryPasswordToAccountHolder(String email, String temp_password) {
        mailProvider.emailAccountTemporaryPassword(email,temp_password);
    }

    @Override
    public void emailPasswordResetToAccountHolder(String email, String confirmURL) {
        mailProvider.emailAccountPasswordResetConfirmationLink(email, confirmURL);

    }
}
