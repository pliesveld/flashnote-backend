package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.spring.BlankEntityTestAnnotations;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(SpringJUnit4ClassRunner.class)
@BlankEntityTestAnnotations
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
    public void whenContextLoad_thenCorrect()
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
