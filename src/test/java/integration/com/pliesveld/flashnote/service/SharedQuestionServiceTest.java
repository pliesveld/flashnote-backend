package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.populator.repository.reader.RepositorySettings;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(name = "REPOSITORY", classes = { SharedQuestionServiceTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class SharedQuestionServiceTest extends AbstractTransactionalServiceUnitTest {

    @Bean
    public RepositorySettings repositorySettings() {
        RepositorySettings repositorySettings = new RepositorySettings(new Resource[] {new ClassPathResource("test-data-shared-question-extra.json", this.getClass()) });
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
        assertEquals(2, deckRepository.count());
        assertEquals(2, flashCardRepository.count());
        assertEquals(1, questionBankRepository.count());
        assertEquals(3, questionRepository.count());
        assertEquals(2, answerRepository.count());
    }

    @Test
    public void givenContextLoad_whenFindEntitiesByName_thenCorrect()
    {
        assertNotNull(categoryRepository.findOne(500));
        assertNotNull(questionRepository.findOne(9000));
        assertNotNull(questionRepository.findOne(9001));
        assertNotNull(questionRepository.findOne(9002));
        assertNotNull(answerRepository.findOne(9003));
        assertNotNull(answerRepository.findOne(9004));
        assertNotNull(questionBankRepository.findOne(1));
        assertNotNull(deckRepository.findOne(2));
        assertNotNull(deckRepository.findOne(3));
    }

    @Test
    @DirtiesContext
    public void givenDeckBankWithSharedQuestion_whenDeleteDeck_thenCorrect() {
        deckService.deleteDeck(2);
        assertDeckRepositoryCount(1);
        assertFlashCardRepositoryCount(1);
        assertQuestionBankRepositoryCount(1);
        assertQuestionRepositoryCount(3);
    }

    @Test
    @DirtiesContext
    public void givenDeckBankWithoutSharedQuestion_whenDeleteDeck_thenCorrect() {
        deckService.deleteDeck(3);
        assertDeckRepositoryCount(1);
        assertFlashCardRepositoryCount(1);
        assertQuestionBankRepositoryCount(1);
        assertQuestionRepositoryCount(2);
        assertAnswerRepositoryCount(1);
    }

    @Test
    @DirtiesContext
    public void givenDeckBankWithSharedQuestion_whenDeleteAllDecksThenBank_thenCorrect() {
        deckService.deleteDeck(2);
        deckService.deleteDeck(3);
        bankService.deleteBank(1);

        assertDeckRepositoryCount(0);
        assertFlashCardRepositoryCount(0);
        assertQuestionBankRepositoryCount(0);
        assertQuestionRepositoryCount(0);
        assertAnswerRepositoryCount(0);
    }

    @Test
    @DirtiesContext
    public void givenDeckBankWithSharedQuestion_whenDeleteAllBankThenDecks_thenCorrect() {
        bankService.deleteBank(1);
        deckService.deleteDeck(2);
        deckService.deleteDeck(3);

        assertDeckRepositoryCount(0);
        assertFlashCardRepositoryCount(0);
        assertQuestionBankRepositoryCount(0);
        assertQuestionRepositoryCount(0);
        assertAnswerRepositoryCount(0);
    }
}


