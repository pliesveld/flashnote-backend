package com.pliesveld.flashnote.unit.dao.repository;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.repository.StudentRepository;
import com.pliesveld.flashnote.unit.dao.config.SpringTestDataConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.*;

/**
 * Created by happs on 3/2/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("h2")
@ContextConfiguration(classes = SpringTestDataConfig.class, loader = AnnotationConfigContextLoader.class)
@TestPropertySource( locations = "classpath:test-datasource.properties" )
public class StudentTest {

    @Autowired
    StudentRepository studentRepository;

    @Test
    public void testLoadRepositoryFromJson()
    {
        assertTrue(studentRepository.count() > 0);
    }

    @Test
    public void testFindByEmail()
    {
        final String STUDENT_EMAIL = "student@example.com";
        Student student = studentRepository.findOneByEmail(STUDENT_EMAIL);
        assertNotNull(student);
        assertEquals(student.getEmail(), STUDENT_EMAIL);
    }


}
