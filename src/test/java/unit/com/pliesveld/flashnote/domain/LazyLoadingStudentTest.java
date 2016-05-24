package com.pliesveld.flashnote.domain;


import com.pliesveld.flashnote.spring.BlankEntityTestAnnotations;
import com.pliesveld.tests.listeners.LogHibernateTestExecutionListener;
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@BlankEntityTestAnnotations
@Transactional
@TestExecutionListeners(listeners = LogHibernateTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class LazyLoadingStudentTest extends AbstractDomainEntityUnitTest {

    @PersistenceContext
    EntityManager entityManager;

    Serializable student_id = null;

    @Before
    public void givenStudentAndDetails()
    {
        student_id = null;

        StudentDetails studentDetails = studentDetailsBean();
        entityManager.persist(studentDetails);

        entityManager.flush();
        entityManager.clear();
        student_id = studentDetails.getId();
    }

    @Test
    public void whenContextLoad_thenCorrect()
    {
        assertNotNull(student_id);
        assertNotNull(entityManager.find(Student.class, student_id));
        assertNotNull(entityManager.find(StudentDetails.class, student_id));
    }

    @After
    public void flushAfter()
    {
        entityManager.flush();
    }

    @Test
    public void givenStudentDetails_whenProxy_thenStudentNotLoaded()
    {
        StudentDetails studentDetails = studentDetailsRepository.getOne((Integer) student_id);
        assertFalse(Hibernate.isInitialized(studentDetails));
        studentDetails.getId();
        assertFalse(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(studentDetails));
        assertFalse(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(studentDetails, "student"));
    }

    @Test
    public void givenStudent_whenProxy_thenStudentDetailsNotLoaded()
    {
        Student student = studentRepository.getOne((Integer) student_id);
        assertFalse(Hibernate.isInitialized(student));
        student.getId();
        assertFalse(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(student));
        assertFalse(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(student, "studentDetails"));
    }

    @Test
    public void givenLoadStudentDetails_whenAttributeReference_thenStudentNotLoaded()
    {
        StudentDetails studentDetails = studentDetailsRepository.findOne((Integer) student_id);
        assertTrue(Hibernate.isInitialized(studentDetails));
        assertTrue(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(studentDetails));
        assertFalse(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(studentDetails, "student"));
    }

    @Test
    public void givenLoadStudent_whenAttributeReference_thenStudentDetailsNotLoaded()
    {
        Student student = studentRepository.findOne((Integer) student_id);
        assertTrue(Hibernate.isInitialized(student));
        assertTrue(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(student));
        assertFalse(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(student, "studentDetails"));
    }

    @Test
    public void givenLoadStudentDetails_whenStudentProxy_thenStudentNotLoaded()
    {
        StudentDetails studentDetails = studentDetailsRepository.findOne((Integer) student_id);
        assertTrue(Hibernate.isInitialized(studentDetails));

        Student student = studentRepository.getOne((Integer) student_id);
        assertFalse(Hibernate.isInitialized(student));
    }

    @Test
    public void givenLoadStudent_whenStudentDetailsProxy_thenStudentDetailsNotLoaded()
    {
        Student student = studentRepository.findOne((Integer) student_id);
        assertTrue(Hibernate.isInitialized(student));

        StudentDetails studentDetails = studentDetailsRepository.getOne((Integer) student_id);
        assertFalse(Hibernate.isInitialized(studentDetails));
    }





}
