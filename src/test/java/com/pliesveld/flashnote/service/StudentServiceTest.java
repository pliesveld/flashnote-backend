package com.pliesveld.flashnote.service;

import com.pliesveld.config.SpringTestConfig;
import com.pliesveld.flashnote.dao.StudentDao;
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
public class StudentServiceTest {

    @Autowired
    StudentServiceImpl studentService;

    @Test
    public void sanityCheck()
    {
        StudentDao studentDao = studentService.getDao();
        assertEquals("Database should have zero students",0,studentDao.getCount());
    }

    @Test
    public void accoutCreation()
    {
        StudentDao studentDao = studentService.getDao();
        assertEquals("Database should have 0 student",0,studentDao.getCount());
        StudentService.CREATE_RESULT result = studentService.create("Student1","student@example.com","password");
        assertEquals(result,StudentService.CREATE_RESULT.SUCCESS);
        assertEquals("Database should have 1 student",1,studentDao.getCount());
    }

    @Test
    public void accoutCreationDuplicate()
    {
       StudentDao studentDao = studentService.getDao();
        assertEquals("Database should have 1 student",0,studentDao.getCount());

        StudentService.CREATE_RESULT result = studentService.create("Student2","student2@example.com","password");
        assertEquals(result,StudentService.CREATE_RESULT.SUCCESS);
        StudentService.CREATE_RESULT result2 = studentService.create("Student2","student2@example.com","password");
        assertEquals(result2,StudentService.CREATE_RESULT.EMAIL_TAKEN);


        assertEquals("Database should have 1 student",1,studentDao.getCount());
    }

}
