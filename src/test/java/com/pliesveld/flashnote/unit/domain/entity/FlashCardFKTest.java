package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.unit.spring.DefaultTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@DefaultTestAnnotations
@Transactional
public class FlashCardFKTest extends FlashCardTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    protected EntityManager entityManager;

    @Before
    @Override
    public void setupEntities()
    {
        super.setupEntities();

        entityManager.flush();
        entityManager.clear(); // !!
    }

    @Test
    public void testSubTestLoad()
    {
    }

    @Test
    @Override
    public void testEntitySanity()
    {
        assertNotNull(flashcard_id);

        FlashCard fc = entityManager.find(FlashCard.class, flashcard_id);
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
        entityManager.flush();
    }

    @Test
    public void testQuestionRemovalFKViolation()
    {
        thrown.expect(PersistenceException.class);
        questionRepository.deleteAll();
    }

    @Test
    public void testAnswerRemovalFKViolation()
    {
        thrown.expect(PersistenceException.class);
        answerRepository.deleteAll();
    }


}
