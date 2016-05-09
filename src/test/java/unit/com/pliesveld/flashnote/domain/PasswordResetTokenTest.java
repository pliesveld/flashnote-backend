package com.pliesveld.flashnote.domain;


import com.pliesveld.flashnote.spring.DefaultEntityTestAnnotations;
import com.pliesveld.tests.listeners.LogHibernateTestExecutionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.io.Serializable;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
@TestExecutionListeners(listeners = LogHibernateTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class PasswordResetTokenTest extends AbstractDomainEntityUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    EntityManager entityManager;

    Serializable student_id = null;
    Serializable token_id = null;
    String token;

    @Before
    public void setupEntities()
    {
        Student student = this.studentBean();
        entityManager.persist(student);
        entityManager.flush();
        student_id = student.getId();
        assertNotNull(student_id);
        token = UUID.randomUUID().toString();
        AccountPasswordResetToken accountPasswordResetToken = new AccountPasswordResetToken(student,token);

        entityManager.persist(accountPasswordResetToken);
        token_id = accountPasswordResetToken.getId();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testEntityContext()
    {
        assertNotNull(student_id);
        assertNotNull(token_id);
        assertEquals(student_id,token_id);
    }

    @Test
    public void testEntitySanity()
    {
        assertStudentRepositoryCount(1);
        assertPasswordResetRepositoryCount(1);
    }

    @After
    public void flushAfter()
    {
        entityManager.flush();
    }

    @Test
    public void testEntityFindToken()
    {
        assertNotNull(passwordResetRepository.findByToken(token));
    }

    @Test
    public void testEntityFindStudent()
    {
        Student student = studentRepository.findOne((Integer) student_id);
        assertNotNull(passwordResetRepository.findByStudent(student));
    }
    @Test
    public void testEntityFindStudentDetached()
    {
        Student student = studentRepository.findOne((Integer) student_id);
        assertNotNull(student);
        entityManager.detach(student);
        entityManager.flush();
        assertNotNull(passwordResetRepository.findByStudent(student));
    }


    @Test
    public void testTokenRemoval()
    {
        passwordResetRepository.delete((Integer) student_id);
        assertStudentRepositoryCount(1);
        assertPasswordResetRepositoryCount(0);
    }


    @Test
    public void testStudentRemovalFKViolation()
    {
        thrown.expect(PersistenceException.class);
        studentRepository.delete((Integer) student_id);
    }


    @Test
    public void testStudentRemoval()
    {
        passwordResetRepository.delete((Integer) student_id);
        studentRepository.delete((Integer) student_id);
        assertPasswordResetRepositoryCount(0);
        assertStudentRepositoryCount(0);
    }

    @Test
    public void testDuplicateToken()
    {
        Student student = studentRepository.findOne((Integer) student_id);
        AccountPasswordResetToken second_token = new AccountPasswordResetToken(student, UUID.randomUUID().toString());

        thrown.expect(PersistenceException.class);
        entityManager.persist(second_token);
    }


}
