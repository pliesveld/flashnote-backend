package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * Created by happs on 3/2/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextConfiguration(classes = SpringDataTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class StudentTest {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    StudentDetailsRepository studentDetailsRepository;

    @Autowired
    QuestionRepository questionRepository;


    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void testLoadRepositoryFromJson()
    {
        assertTrue(studentRepository.count() > 0);
        assertTrue(questionRepository.count() > 0);
        assertTrue(categoryRepository.count() > 0);

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
    public void testFindCategoryInJson()
    {

        assertTrue(categoryRepository.count() > 0);

        Category category = categoryRepository.findOne(5);
        assertNotNull(category);
    }

}



