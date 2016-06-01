package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.spring.Profiles;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
public class QueriesTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void whenFindQuestionByJPQL() {
        TypedQuery<Question> query = entityManager.createQuery("select q from Question q where q.id = :id", Question.class);
        query.setParameter("id", 9000);
        Question question = query.getSingleResult();
        assertNotNull(question);
    }

    @Test
    public void whenFindAnswerByJPQL() {
        TypedQuery<Answer> query = entityManager.createQuery("select a from Answer a where a.id = :id", Answer.class);
        query.setParameter("id", 9001);
        Answer answer = query.getSingleResult();
        assertNotNull(answer);
    }


    public List<FlashCardPrimaryKey> findFlashCardPrimaryKeyByAnswerIdByJPQL() {
        TypedQuery<FlashCardPrimaryKey> query = entityManager.createQuery("select f.id from FlashCard f where f.id.answerId = :id", FlashCardPrimaryKey.class);
        query.setParameter("id", 9001);
        List<FlashCardPrimaryKey> flashCardPrimaryKeyList = query.getResultList();
        return flashCardPrimaryKeyList;
    }

    @Test
    public void whenBankByQuestionId() {
        TypedQuery<Long> query = entityManager.createQuery("select count(*) from QuestionBank qb inner join qb.questions qc where qc.id = :questionId", Long.class);
        query.setParameter("questionId", 9000);
        long result = query.getSingleResult();
        assertEquals(1, result);
    }

    @Test
    public void whenDeckByQuestionId() {

        TypedQuery<Long> query = entityManager.createQuery("select count(*) from Deck d inner join d.flashcards fc where fc.id.questionId = :questionId", Long.class);
        query.setParameter("questionId", 9000);
        long result = query.getSingleResult();
        assertEquals(1, result);
    }

    @Test
    public void whenDeckByAnswerId() {

        TypedQuery<Long> query = entityManager.createQuery("select count(*) from Deck d inner join d.flashcards fc where fc.id.answerId = :answerId", Long.class);
        query.setParameter("answerId", 9001);
        long result = query.getSingleResult();
        assertEquals(1, result);
    }

}
