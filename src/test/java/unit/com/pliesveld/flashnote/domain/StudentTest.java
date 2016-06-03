package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.spring.BlankEntityTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@BlankEntityTestAnnotations
@Transactional
public class StudentTest extends AbstractDomainEntityUnitTest {
    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext
    protected EntityManager entityManager;
    protected Serializable student_id = null;
    protected String student_email = null;

    @Before
    public void setupEntities()
    {
        Student student = studentBean();

        entityManager.persist(student);

        student_id = student.getId();
        student_email = student.getEmail();

        /*
            Do not call flush / clear
            subclasses of this test expect student to
            be in the persistence context
         */
//        entityManager.flush();
//        entityManager.clear();
    }

    @Test
    public void whenContextLoad_thenCorrect()
    {
        assertNotNull(student_id);
        assertNotNull(student_email);
        assertNotNull(entityManager.getReference(Student.class,student_id));
        assertStudentRepositoryCount(1);
    }

    @Test
    public void givenStudent_whenFindByEmail()
    {
        entityManager.flush();
        entityManager.clear();

        assertNotNull(studentRepository.findOneByEmail(student_email));
    }

    @Test
    public void givenStudent_whenFindById()
    {
        entityManager.flush();
        entityManager.clear();

        assertNotNull(studentRepository.findOne((Integer) student_id));
    }

}

