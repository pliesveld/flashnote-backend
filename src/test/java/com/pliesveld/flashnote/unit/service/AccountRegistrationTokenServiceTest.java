package com.pliesveld.flashnote.unit.service;

import com.pliesveld.flashnote.domain.AccountRegistrationToken;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.flashnote.repository.RegistrationRepository;
import com.pliesveld.flashnote.repository.StudentDetailsRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import com.pliesveld.flashnote.service.AccountRegistrationService;
import com.pliesveld.flashnote.service.StudentService;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.unit.spring.SpringMailServiceTestConfig;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = {SpringMailServiceTestConfig.class}, loader = AnnotationConfigContextLoader.class)
public class AccountRegistrationTokenServiceTest {
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    AccountRegistrationService accountRegistrationService;

    @Autowired
    StudentService studentService;

    @Autowired
    StudentDetailsRepository studentDetailsRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    RegistrationRepository registrationRepository;

    @Before
    @Transactional
    public void removeAccountInfo()
    {
        registrationRepository.deleteAll();
        studentDetailsRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    public void accoutCreation()
    {
        assertEquals(0, accountRegistrationService.countAccountRegistration());
        assertEquals(0, accountRegistrationService.countStudent());
        assertEquals(0, accountRegistrationService.countStudentDetails());
        assertNotNull(accountRegistrationService.createStudent("Student1", "student@example.com", "password"));
        assertEquals(0, accountRegistrationService.countAccountRegistration());
        assertEquals(1, accountRegistrationService.countStudent());
        assertEquals(1, accountRegistrationService.countStudentDetails());
    }

    @Test
    public void accoutCreationDuplicate()
    {
        assertEquals(0, accountRegistrationService.countAccountRegistration());
        assertEquals(0, accountRegistrationService.countStudent());
        assertEquals(0, accountRegistrationService.countStudentDetails());
        assertNotNull(accountRegistrationService.createStudent("Student2", "student2@example.com", "password"));
        thrown.expect(StudentCreateException.class);
        accountRegistrationService.createStudent("Student2", "student2@example.com", "password");
    }

    @Test
    public void accountStudentEmailExistsRegistrationCreation()
    {
        String name = "TESTUSER";
        String email = "unit-test@example.com";
        String password = "123456";

        Student student = accountRegistrationService.createStudent(name, email, password);

        assertEquals(0, accountRegistrationService.countAccountRegistration());
        assertEquals(1, accountRegistrationService.countStudent());

        thrown.expect(StudentCreateException.class);
        accountRegistrationService.createStudent(name, email, password);
    }


    @Test
    public void accountCreationAndRemoval()
    {
        Student student = accountRegistrationService.createStudent("Student1", "student@example.com", "password");
        assertNotNull(student);
        AccountRegistrationToken registration = accountRegistrationService.createAccountRegistration(student);
        assertNotNull(registration);

        assertEquals(1, accountRegistrationService.countAccountRegistration());
        assertEquals(1, accountRegistrationService.countStudent());
        assertEquals(1, accountRegistrationService.countStudentDetails());

        assertNotNull(accountRegistrationService.deleteAccountRegistration(student.getId()));
        assertNotNull(studentService.delete(student.getId()));
        assertEquals(0, accountRegistrationService.countAccountRegistration());
        assertEquals(0, accountRegistrationService.countStudent());
        assertEquals(0, accountRegistrationService.countStudentDetails());
    }
}
