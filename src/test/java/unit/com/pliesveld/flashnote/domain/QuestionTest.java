package com.pliesveld.flashnote.domain;


import com.pliesveld.flashnote.spring.BlankEntityTestAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@BlankEntityTestAnnotations
@Transactional
final public class QuestionTest extends com.pliesveld.flashnote.domain.AbstractTransactionalDomainEntityUnitTest {

    Integer que_id = null;

    @Before
    public void givenQuestion() {
        Question question = new Question(UUID.randomUUID().toString());
        entityManager.persist(question);
        entityManager.flush();
        que_id = question.getId();

    }

    @After
    public void givenClearedContext() {
        entityManager.clear();
    }

    @Test
    public void whenContextLoad_thenCorrect() {
        assertNotNull(que_id);
        assertQuestionRepositoryCount(1);
    }


    @Test
    public void givenClearedContext_whenProxy_thenCorrect() {
        entityManager.clear();
        Question question = questionRepository.getOne(que_id);
        Question question2 = questionRepository.getOne(que_id);
        assertNotNull(question);
        assertEquals(question, question2);
    }

    @Test
    public void givenClearedContext_whenProxyGetId_thenCorrect() {
        entityManager.clear();
        Question question = questionRepository.getOne(que_id);
        assertNotNull(question);
        question.getId();
    }

    @Test
    public void givenClearedContext_whenLoad_thenCorrect() {
        entityManager.clear();
        Question question = questionRepository.findOne(que_id);
        assertNotNull(question);
        entityManager.detach(question);
    }

}
