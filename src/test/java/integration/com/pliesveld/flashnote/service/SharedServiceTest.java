package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.spring.Profiles;

import com.pliesveld.flashnote.spring.repository.RepositorySettings;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.persistence.*;

import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(name = "REPOSITORY", classes = { SharedServiceTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class SharedServiceTest extends AbstractTransactionalServiceUnitTest {

    @Bean
    public RepositorySettings repositorySettings() {
        RepositorySettings repositorySettings = new RepositorySettings(new Resource[] {new ClassPathResource("test-data-shared-question-ref.json", this.getClass()) });
        return repositorySettings;
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private CardService cardService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private BankService bankService;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    public void whenContextLoad_thenCorrect()
    {
        assertEquals(1, deckRepository.count());
        assertEquals(1, flashCardRepository.count());
        assertEquals(1, questionBankRepository.count());
        assertEquals(1, questionRepository.count());
        assertEquals(1, answerRepository.count());
    }

    @Test
    public void givenContextLoad_whenFindEntitiesByName_thenCorrect()
    {
        assertNotNull(categoryRepository.findOne(500));
        assertNotNull(questionRepository.findOne(9000));
        assertNotNull(answerRepository.findOne(9001));
        assertNotNull(questionBankRepository.findOne(1));
        assertNotNull(deckRepository.findOne(2));
    }

    @Test
    public void whenFindQuestionByJPQL() {
        TypedQuery<Question> query = entityManager.createQuery("select q from Question q where q.id = :id", Question.class);
        query.setParameter("id", 9000);
        Question question = query.getSingleResult();
        assertNotNull(question);
        debugEntity(question);
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

    @Test
    @DirtiesContext
    public void givenSharedQuestion_whenDeleteQuestion_thenCorrect() {
        cardService.deleteQuestion(9000);
    }

    @Test
    @DirtiesContext
    public void whenDeleteAnswer_thenCorrect() {
        cardService.deleteAnswer(9001);
    }


    @Test
    @DirtiesContext
    public void whenDeleteDeck_thenCorrect() {
        deckService.deleteDeck(2);
    }

    @Test
    @DirtiesContext
    public void whenDeleteBank_thenCorrect() {
        bankService.deleteQuestionBank(1);
    }


}


