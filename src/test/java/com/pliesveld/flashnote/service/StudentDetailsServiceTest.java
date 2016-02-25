package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.spring.SpringTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

/**
 * Created by happs on 1/15/16.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class StudentDetailsServiceTest {
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();

    @Autowired
    StudentService studentService;

    @Test
    public void sanityCheck()
    {
        assertEquals("Database should have zero students",0,studentService.count().hashCode());
    }

    @Test
    public void accoutCreation()
    {
        assertEquals("Database should have 0 student",0,studentService.count().hashCode());
        studentService.create("Student1","student@example.com","password");
        assertEquals("Database should have 1 student",1,studentService.count().hashCode());
    }

    @Test(expected = StudentCreateException.class)
    public void accoutCreationDuplicate()
    {
        assertEquals("Database should have 0 student",0,studentService.count().hashCode());

        studentService.create("Student2","student2@example.com","password");
        studentService.create("Student2","student2@example.com","password");

        assertEquals("Database should have 1 student",1,studentService.count().hashCode());
    }

    @Test
    public void accountCreationAndRemoval()
    {
        assertEquals("Database should have 0 student",0,studentService.count().hashCode());
        Student student = studentService.create("Student1","student@example.com","password");
        assertEquals("Database should have 1 student",1,studentService.count().hashCode());
        studentService.delete(student.getId());
        assertEquals("Database should have 0 student",0,studentService.count().hashCode());
    }
}
