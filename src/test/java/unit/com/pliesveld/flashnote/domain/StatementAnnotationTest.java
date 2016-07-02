package com.pliesveld.flashnote.domain;


import com.pliesveld.flashnote.spring.BlankEntityTestAnnotations;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUtil;
import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@BlankEntityTestAnnotations
@Transactional
public class StatementAnnotationTest extends AbstractDomainEntityUnitTest {

    @PersistenceContext
    EntityManager entityManager;

    Serializable question_id = null;
    Serializable answer_id = null;
    Serializable student_id = null;

    private static final String MESSAGE = UUID.randomUUID().toString();


    @Before
    public void setupEntities() {
        Question question = this.questionBean();
        Answer answer = this.answerBean();
        Student student = this.studentBean();

        question = questionRepository.save(question);
        answer = answerRepository.save(answer);
        student = studentRepository.save(student);

        question_id = question.getId();
        answer_id = answer.getId();
        student_id = student.getId();

        {
            AnnotatedStatement annotatedStatement = new AnnotatedStatement(student, MESSAGE);
            question.addAnnotation(annotatedStatement);
        }
        {
            AnnotatedStatement annotatedStatement = new AnnotatedStatement(student, MESSAGE);
            answer.addAnnotation(annotatedStatement);
        }

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testEntityContext() {
        assertNotNull(question_id);
        assertNotNull(answer_id);
        assertNotNull(student_id);
    }

    @Test
    public void whenContextLoad_thenCorrect() {
        assertNotNull(entityManager.find(Question.class, question_id));
        assertNotNull(entityManager.find(Answer.class, answer_id));
        assertNotNull(entityManager.find(Student.class, student_id));

        this.assertQuestionRepositoryCount(1);
        this.assertAnswerRepositoryCount(1);
        this.assertStatementRepositoryCount(2);
    }

    @Test
    public void testQuestionContainsAnnotation() {
        Question question = entityManager.find(Question.class, question_id);
        assertEquals(1, question.getAnnotations().size());
    }

    @Test
    public void testAnswerContainsAnnotation() {
        Answer answer = entityManager.find(Answer.class, answer_id);
        assertEquals(1, answer.getAnnotations().size());
    }

    @Test
    public void testQuestionAnnotationRemoval() {
        {
            Question question = entityManager.find(Question.class, question_id);
            assertEquals(1, question.getAnnotations().size());
            question.getAnnotations().clear();
            entityManager.flush();
            entityManager.clear();
        }

        Question question = entityManager.find(Question.class, question_id);
        assertEquals(0, question.getAnnotations().size());

    }


    @Test
    public void testAnnotationIsLazyLoaded() {
        Question question = entityManager.find(Question.class, question_id);
        assertFalse(Hibernate.isInitialized(question.getAnnotations()));
        assertFalse(Hibernate.isInitialized(question.getAnnotations()));


        assertTrue(Hibernate.isPropertyInitialized(question, "id"));

        Collection<AnnotatedStatement> annotations = question.getAnnotations();

        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        assertFalse(persistenceUtil.isLoaded(question, "annotations"));


        assertFalse(Hibernate.isInitialized(question.getAnnotations()));
        //entityManager.unwrap(Session.class).createQuery("");
    }


    @Test
    public void testAnnotationIsLazyLoaded_whenGetReference() {
        Question question = entityManager.getReference(Question.class, question_id);
        assertFalse(Hibernate.isInitialized(question));
        Collection<AnnotatedStatement> annotations = question.getAnnotations();

        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        assertFalse(persistenceUtil.isLoaded(question, "annotations"));

        //entityManager.unwrap(Session.class).createQuery("");
    }

    @After
    public void flushAfter() {
        entityManager.flush();
    }

}
