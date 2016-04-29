package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class StudentTest extends AbstractDomainEntityUnitTest {
    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext
    protected EntityManager entityManager;
    protected Serializable student_id = null;

    @Before
    public void setupEntities()
    {
        Student student = studentBean();
        assertNotNull(student);
        assertNull(student.getStudentDetails());

        student = studentRepository.save(student);
        student_id = student.getId();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testEntitySanity()
    {
        assertNotNull(student_id);
        assertNotNull(entityManager.find(Student.class,student_id));
    }

    @After
    public void flushAfter()
    {
        entityManager.flush();
    }
}

