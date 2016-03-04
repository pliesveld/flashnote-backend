package com.pliesveld.flashnote.unit.dao.repository;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.flashnote.repository.StudentDetailsRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.unit.dao.config.SpringTestDataConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by happs on 3/2/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = SpringTestDataConfig.class, loader = AnnotationConfigContextLoader.class)
@TestPropertySource( locations = "classpath:test-datasource.properties" )

@Transactional
public class StudentTest {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentDetailsRepository studentDetailsRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Test
    public void testLoadRepositoryFromJson()
    {
        assertTrue(studentRepository.count() > 0);
        assertTrue(studentDetailsRepository.count() > 0);
    }

    @Test
    public void testFindByEmail()
    {
        final String STUDENT_EMAIL = "student@example.com";
        Student student = studentRepository.findOneByEmail(STUDENT_EMAIL);
        assertNotNull(student);
        assertEquals(student.getEmail(), STUDENT_EMAIL);
    }

    @Test
    public void testFindStudentById()
    {
        Student student = studentRepository.findOne(1);
        assertNotNull(student);
    }

    @Test
    public void testFindStudentDetailsById()
    {
        StudentDetails studentDetails = studentDetailsRepository.findOne(1);
        assertNotNull(studentDetails);
        assertEquals(studentDetails.getName(),"account");
    }



}
