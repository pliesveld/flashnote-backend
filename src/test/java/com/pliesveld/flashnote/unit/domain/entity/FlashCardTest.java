package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
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
public class FlashCardTest extends AbstractDomainEntityUnitTest
{
    @PersistenceContext
    protected EntityManager entityManager;

    protected Serializable flashcard_id;

    @Before
    public void setupEntities()
    {
        Question question = questionBean();
        Answer answer = answerBean();

        entityManager.persist(question);
        entityManager.persist(answer);

        entityManager.flush();

        FlashCard flashCard = new FlashCard(question,answer);
        entityManager.persist(flashCard);

        flashcard_id = flashCard.getId();
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testTestLoad()
    {

    }

    @Test
    public void testEntitySanity()
    {
        assertNotNull(flashcard_id);

        FlashCard fc = flashCardRepository.findAll().iterator().next();
        assertNotNull(fc);
        assertNotNull(fc.getAnswer());
        assertNotNull(fc.getQuestion());
        assertTrue(questionRepository.exists(fc.getQuestion().getId()));
        assertTrue(answerRepository.exists(fc.getAnswer().getId()));

        assertQuestionRepositoryCount(1);
        assertAnswerRepositoryCount(1);
        assertFlashCardRepositoryCount(1);
    }

    @After
    public void flushAfter()
    {
        LOG.debug("flushAfter");
        entityManager.flush();
    }

    @Test
    public void testFlashCardRemoval()
    {
        flashCardRepository.deleteAll();
    }

    @Test
    public void testAnswerRemoval()
    {
        flashCardRepository.deleteAll();
        answerRepository.deleteAll();
    }

    @Test
    public void testQuestionRemoval()
    {
        flashCardRepository.deleteAll();
        questionRepository.deleteAll();
    }



    /*
    @Test
    public void testAnswerModifications()
    {
        final String ANSWER_ORIG = "original answer.";
        final String ANSWER_MODIFIED = "modified answer.";


        Question q = new Question("This is a question?");
        Question q2 = new Question("This is a question?");

        entityManager.persist(q);
        entityManager.persist(q2);
        entityManager.flush();

        Serializable q_id = q.getId();
        Serializable q2_id = q2.getId();

        Answer a = new Answer();
        a.setContent(ANSWER_ORIG);
        entityManager.persist(a);
        entityManager.flush();
        Serializable a_id = a.getId();

        FlashCard fc = new FlashCard(q,a);
        entityManager.persist(fc);
        entityManager.flush();
        Serializable fc_id = fc.getId();

        FlashCard fc2 = new FlashCard(q2,a);
        entityManager.persist(fc2);
        entityManager.flush();
        Serializable fc2_id = fc2.getId();

        Answer a_loaded = entityManager.find(Answer.class,a_id);
        a_loaded.setContent(ANSWER_MODIFIED);

        assertEquals("Loaded answer does not change content of original reference",ANSWER_MODIFIED,a.getContent());
        assertEquals("Referenced flashcard answer does not change original",ANSWER_MODIFIED,fc.getAnswer().getContent());

        //session.update(a_loaded);

        assertEquals("After answer updated, original reference to flashcard does not match modified",ANSWER_MODIFIED,fc.getAnswer().getContent());
        assertEquals("After answer updated, original reference to flashcard does not match modified",ANSWER_MODIFIED,fc2.getAnswer().getContent());

        FlashCard fc3 = entityManager.find(FlashCard.class,fc2_id);

        assertEquals("After answer updated, loaded flashcard does not match modified",ANSWER_MODIFIED,fc3.getAnswer().getContent());

         
    }*/





}
