package com.pliesveld.flashnote.domain;


import com.pliesveld.flashnote.unit.dao.spring.LogHibernateTestExecutionListener;
import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
@TestExecutionListeners(listeners = LogHibernateTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class LazyLoadingStudentTest extends AbstractDomainEntityUnitTest {

    @PersistenceContext
    EntityManager entityManager;

    Serializable student_id = null;

    @Before
    public void setupEntities()
    {
        student_id = null;

        Student student = studentBean();
        StudentDetails studentDetails = studentDetailsBean();
        entityManager.persist(student);

        student_id = student.getId();
        assertNotNull(student_id);

//        studentDetails.setId((Integer) student_id);
        studentDetails.setStudent(student);
        entityManager.persist(studentDetails);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testEntityContext()
    {
        assertNotNull(student_id);
    }

    @Test
    public void testEntitySanity()
    {

        assertNotNull(entityManager.find(Student.class, student_id));
        assertNotNull(entityManager.find(StudentDetails.class, student_id));
    }

    @After
    public void flushAfter()
    {
        entityManager.flush();
    }
    @Test
    public void givenStudentDetails_whenLoading_thenStudentNotLoaded()
    {
        StudentDetails studentDetails = studentDetailsRepository.getOne((Integer) student_id);
        assertFalse(Hibernate.isInitialized(studentDetails));
        studentDetails.getId();
        assertTrue(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(studentDetails));
        assertFalse(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(studentDetails, "student"));
    }

    @Test
    public void givenStudent_whenLoading_thenStudentDetailsNotLoaded()
    {
        Student student = studentRepository.getOne((Integer) student_id);
        assertFalse(Hibernate.isInitialized(student));
        student.getId();
        assertTrue(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(student));
        assertFalse(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(student, "studentDetails"));
    }


}
