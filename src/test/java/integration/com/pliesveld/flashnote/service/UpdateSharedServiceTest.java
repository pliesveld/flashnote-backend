package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.QuestionBank;
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
        @ContextConfiguration(name = "REPOSITORY", classes = {UpdateSharedServiceTest.class}, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class UpdateSharedServiceTest extends AbstractTransactionalServiceUnitTest {

    @Bean
    public RepositorySettings repositorySettings() {
        RepositorySettings repositorySettings = new RepositorySettings(new Resource[]{new ClassPathResource("test-data-deck-update.json", this.getClass())});
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
    public void whenContextLoad_thenCorrect() {
        assertEquals(1, deckRepository.count());
        assertEquals(1, flashCardRepository.count());
        assertEquals(1, questionBankRepository.count());
        assertEquals(2, questionRepository.count());
        assertEquals(1, answerRepository.count());
    }

    @Test
    public void givenContextLoad_whenFindEntitiesByName_thenCorrect() {
        assertNotNull(categoryRepository.findOne(500));
        assertNotNull(categoryRepository.findOne(501));
        assertNotNull(questionRepository.findOne(9000));
        assertNotNull(questionRepository.findOne(9001));
        assertNotNull(answerRepository.findOne(9002));
        assertNotNull(questionBankRepository.findOne(1));
        assertNotNull(deckRepository.findOne(2));
    }

    @Test
    @DirtiesContext
    public void givenBank_whenChangeCategory_thenCorrect() {
        Deck deck = deckService.findDeckById(2);
        Category category = categoryRepository.getOne(501);
        deck.setCategory(category);
        deck = deckService.createDeck(deck);
        assertNotNull(deck);
        assertNotNull(deck.getCategory());
        assertEquals(501, (long) deck.getCategory().getId());
    }

    @Test
    @DirtiesContext
    public void givenDeck_whenChangeCategory_thenCorrect() {
        QuestionBank bank = bankService.findQuestionBankById(1);
        Category category = categoryRepository.getOne(501);
        bank.setCategory(category);
        bank = bankService.createQuestionBank(bank);

        assertNotNull(bank);
        assertNotNull(bank.getCategory());
        assertEquals(501, (long) bank.getCategory().getId());
    }
}


