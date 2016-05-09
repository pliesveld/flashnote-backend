package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.spring.DefaultEntityTestAnnotations;
import org.hibernate.Hibernate;
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
import javax.persistence.PersistenceException;
import java.io.Serializable;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class QuestionAttachmentTextTest extends AbstractDomainEntityUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    EntityManager entityManager;

//    @Autowired
//    AttachmentService attachmentService;

    Serializable attachment_id;
    Serializable question_id;

    @Before
    public void setupEntities()
    {
        Question que = new Question("que?");
        que = questionRepository.save(que);
        assertNotNull(que);
        entityManager.flush();
        question_id = que.getId();

        AttachmentText attachmentText = attachmentTextBean();
        attachmentText = attachmentTextRepository.save(attachmentText);
        attachment_id = attachmentText.getId();
        entityManager.flush();

        que.setAttachment(attachmentText);
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testEntitySanity()
    {
        assertNotNull(question_id);
        assertNotNull(attachment_id);
        assertNotNull(entityManager.find(Question.class,question_id));

        assertNotNull(entityManager.find(AbstractAttachment.class,attachment_id));

        assertNotNull(entityManager.find(Question.class,question_id).getAttachment());
        assertEquals(attachment_id, entityManager.find(Question.class, question_id).getAttachment().getId());
    }

    @Test
    public void givenTextAttachment_whenLoadingQuestionById()
    {
        enableSQL();
        Question question = questionRepository.findOneById((Integer) question_id);
        assertNotNull(question);
        assertTrue(Hibernate.isInitialized(question));
        assertTrue(Hibernate.isPropertyInitialized(question, "attachment"));
    }

    @Test
    public void givenTextAttachment_whenLazyLoadingQuestionById()
    {
        Question question = questionRepository.findOne((Integer) question_id);
        assertNotNull(question);
        assertTrue(Hibernate.isInitialized(question));
        assertTrue(Hibernate.isPropertyInitialized(question, "attachment"));
    }


    @Test
    public void givenTextAttachment_whenLoading_andReferencingById()
    {
        Question question = questionRepository.findOneById((Integer) question_id);
        assertNotNull(question);
        assertTrue(Hibernate.isInitialized(question));
        assertTrue(Hibernate.isPropertyInitialized(question, "attachment"));

        question.getId();
        question.getAttachment().getId();
    }

    @Test
    public void givenTextAttachment_whenDeletingAttachment_thenFKViolation()
    {
        thrown.expect(PersistenceException.class);
        attachmentRepository.delete((Integer) attachment_id);
    }

//    @Test
//    public void givenTextAttachment_whenDeletingAttachmentByService_thenCorrect()
//    {
//        attachmentService.removeAttachmentById((Integer) attachment_id);
//        assertQuestionRepositoryCount(1);
//        assertAttachmentRepositoryCount(0);
//    }


    @Test
    public void givenTextAttachment_whenLoading_andIniitializingHibernateProxy()
    {
        Question question = questionRepository.findOneById((Integer) question_id);
        assertNotNull(question);
        assertTrue(Hibernate.isInitialized(question));
        assertTrue(Hibernate.isPropertyInitialized(question, "attachment"));
        Hibernate.initialize(question);
        Hibernate.initialize(question.getAttachment());
    }

    @After
    public void flushAfter()
    {
        entityManager.flush();
    }

}
