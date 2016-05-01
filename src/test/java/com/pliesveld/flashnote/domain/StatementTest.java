package com.pliesveld.flashnote.domain;


import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class StatementTest extends AbstractDomainEntityUnitTest {

    @PersistenceContext
    EntityManager entityManager;

    Serializable entity_id = null;

    @Test
    public void whenSavingQuestion_thenCorrect()
    {
        Question que = new Question("que?");
        que = questionRepository.save(que);
        assertNotNull(que);
    }

    @Test
    public void whenSavingAnswer_thenCorrect()
    {
        Answer ans = new Answer("Ans.");
        ans = answerRepository.save(ans);
        assertNotNull(ans);
    }

    @Test
    public void findByEmail()
    {
        Question que = new Question("que?");
        Answer ans = new Answer("Ans.");

        questionRepository.save(que);
        answerRepository.save(ans);

        assertEquals(1, questionRepository.findAllByAuthor("SYSTEM").count());
        assertEquals(1, answerRepository.findAllByAuthor("SYSTEM").count());
        assertEquals(2, statementRepository.findAllByAuthor("SYSTEM").count());
    }

    @After
    public void flushAfter()
    {
        entityManager.flush();
    }

}
