package com.pliesveld.flashnote.domain;


import com.pliesveld.flashnote.spring.DefaultEntityTestAnnotations;
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
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class StudentCascadeTest extends AbstractDomainEntityUnitTest {

    @PersistenceContext
    EntityManager entityManager;

    Serializable student_id = null;

    @Before
    public void setupEntities()
    {

        Student student = this.studentBean();
        entityManager.persist(student);
        entityManager.persist(student.getStudentDetails());

        student_id = student.getId() ;
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testEntityContext()
    {
    }

    @Test
    public void testEntitySanity()
    {

        assertNotNull(entityManager.find(Student.class, student_id));
        assertNotNull(entityManager.find(StudentDetails.class, student_id));
    }

    @Test
    public void removalDetailsCascadesToAccountTest()
    {
        assertStudentRepositoryCount(1);
        assertStudentDetailsRepositoryCount(1);

        StudentDetails studentDetails = studentDetailsRepository.findOne((Integer) student_id);
        assertTrue(entityManager.contains(studentDetails));
        entityManager.remove(studentDetails);
        entityManager.flush();

        assertStudentDetailsRepositoryCount(0);
        assertStudentRepositoryCount(0);
    }

    @Test
    public void removalAccountCascadesToDetailsTest()
    {
        assertStudentRepositoryCount(1);
        assertStudentDetailsRepositoryCount(1);

        Student student = studentRepository.findOne((Integer) student_id);
        studentRepository.delete(student);
        entityManager.flush();

        assertStudentRepositoryCount(0);
        assertStudentDetailsRepositoryCount(0);
    }


    @After
    public void flushAfter()
    {
        entityManager.flush();
    }

}
