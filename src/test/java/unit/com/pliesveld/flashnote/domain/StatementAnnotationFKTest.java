package com.pliesveld.flashnote.domain;


import com.pliesveld.flashnote.spring.DefaultEntityTestAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class StatementAnnotationFKTest extends AbstractDomainEntityUnitTest {

    private static final String MESSAGE = UUID.randomUUID().toString();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    EntityManager entityManager;

    Serializable question_id = null;
    Serializable student_id = null;

    @Before
    public void setupEntities()
    {
        Question question = this.questionBean();

        StudentDetails studentDetails = this.studentDetailsAndStudentBean();

        question = questionRepository.save(question);
        question_id = question.getId();

        studentDetails = studentDetailsRepository.save(studentDetails);
        student_id = studentDetails.getId();

        AnnotatedStatement annotatedStatement = new AnnotatedStatement(studentDetails,MESSAGE);
        question.addAnnotation(annotatedStatement);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testEntityContext()
    {
        assertNotNull(student_id);
        assertNotNull(question_id);
    }

    @Test
    public void testEntitySanity()
    {
        assertNotNull(entityManager.find(Question.class, question_id));
        assertNotNull(entityManager.find(StudentDetails.class,student_id));

        this.assertQuestionRepositoryCount(1);
        this.assertStatementRepositoryCount(1);
    }

    @Test
    public void testAnnotationRemovalCascade()
    {
        this.questionRepository.deleteAll();
        this.assertQuestionRepositoryCount(0);
        this.assertStatementRepositoryCount(0);
    }

    @After
    public void flushAfter()
    {
        entityManager.flush();
    }

}
