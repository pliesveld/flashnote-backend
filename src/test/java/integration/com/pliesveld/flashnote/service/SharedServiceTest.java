package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.populator.repository.reader.RepositorySettings;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(name = "REPOSITORY", classes = { SharedServiceTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class SharedServiceTest extends AbstractTransactionalServiceUnitTest {

    @Bean
    public RepositorySettings repositorySettings() {
        RepositorySettings repositorySettings = new RepositorySettings(new Resource[] {new ClassPathResource("test-data-shared-question.json", this.getClass()) });
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

    @Test(expected = FlashCardCreateException.class)
    @DirtiesContext
    public void whenAddExistingFlashcardToDeck_thenCorrect() {
        Question question = questionRepository.findOne(9000);
        Answer answer = answerRepository.findOne(9001);
        assertNotNull(question);
        assertNotNull(answer);
        FlashCard flashcard = new FlashCard(question,answer);
        deckService.updateDeckAddFlashCard(2, flashcard);


        assertQuestionBankRepositoryCount(1);
        assertDeckRepositoryCount(1);
        assertQuestionRepositoryCount(1);
        assertAnswerRepositoryCount(1);
        assertFlashCardRepositoryCount(1);
    }

    @Test
    @DirtiesContext
    @Ignore("todo: remove Question by CardService")
    public void givenSharedQuestion_whenDeleteQuestion_thenCorrect() {
        cardService.deleteQuestion(9000);
    }

    @Test
    @DirtiesContext
    @Ignore("todo: remove Answer by CardService")
    public void whenDeleteAnswer_thenCorrect() {
        cardService.deleteAnswer(9001);
    }

    @Test
    @DirtiesContext
    public void whenDeleteDeck_thenCorrect() {
        deckService.deleteDeck(2);
        assertDeckRepositoryCount(0);
        assertFlashCardRepositoryCount(0);
        assertAnswerRepositoryCount(0);
        assertQuestionBankRepositoryCount(1);
        assertQuestionRepositoryCount(1);
    }

    @Test
    @DirtiesContext
    public void whenDeleteBank_thenCorrect() {
        bankService.deleteBank(1);

        assertQuestionBankRepositoryCount(0);
        assertDeckRepositoryCount(1);
        assertFlashCardRepositoryCount(1);
        assertQuestionRepositoryCount(1);
        assertAnswerRepositoryCount(1);
    }

    @Test
    @DirtiesContext
    public void whenDeleteBankThenDeck_thenCorrect() {
        bankService.deleteBank(1);
        deckService.deleteDeck(2);

        assertQuestionBankRepositoryCount(0);
        assertDeckRepositoryCount(0);
        assertFlashCardRepositoryCount(0);
        assertQuestionRepositoryCount(0);
        assertAnswerRepositoryCount(0);
    }

    @Test
    @DirtiesContext
    public void whenDeleteDeckThenBank_thenCorrect() {
        deckService.deleteDeck(2);
        bankService.deleteBank(1);

        assertQuestionBankRepositoryCount(0);
        assertDeckRepositoryCount(0);
        assertFlashCardRepositoryCount(0);
        assertQuestionRepositoryCount(0);
        assertAnswerRepositoryCount(0);
    }

    @Test
    public void whenFindQuestionByBank_thenCorrect() {
        Question question = bankService.findQuestion(1, 9000);
        assertNotNull(question);
        assertNotNull(question.getId());
        assertNotNull(question.getContent());
        assertNotNull(question.getAnnotations());
        assertEquals(0, question.getAnnotations().size());
    }

    @Test
    public void whenFindQuestionByDeck_thenCorrect() {
        Deck deck = deckService.findDeckById(2);
        assertNotNull(deck);
        assertNotNull(deck.getId());
        assertNotNull(deck.getCategory());
        assertNotNull(deck.getDescription());
        assertNotNull(deck.getFlashcards());
        assertEquals(1, deck.getFlashcards().size());
    }

    @Test
    public void whenAllBank_thenCorrect() {
        List<QuestionBank> bankList = bankService.findAllQuestionBanks();
        assertNotNull(bankList);
        assertEquals(1, bankList.size());
        for(QuestionBank questionBank : bankList )
        {
            assertNotNull(questionBank);
            assertNotNull(questionBank.getDescription());
            assertNotNull(questionBank.getId());
            assertNotNull(questionBank.getCategory());
        }
    }

    @Test
    public void whenPagedBank_thenCorrect() {
        Pageable page = new PageRequest(0,1);
        Page<QuestionBank> bankPage = bankService.browseBanks(page);
        assertNotNull(bankPage);

        List<QuestionBank> bankList = bankPage.getContent();
        for(QuestionBank questionBank : bankList )
        {
            assertNotNull(questionBank);
            assertNotNull(questionBank.getDescription());
            assertNotNull(questionBank.getId());
            assertNotNull(questionBank.getCategory());
        }
    }

}


