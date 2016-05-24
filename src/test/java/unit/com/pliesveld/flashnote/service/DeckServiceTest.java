package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.repository.PopulatedCategoriesRepositoryTest;
import com.pliesveld.flashnote.repository.PopulatedDecksRepositoryTest;
import com.pliesveld.flashnote.repository.PopulatedQuestionBanksRepositoryTest;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringServiceTestConfig;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(name = "REPOSITORY", classes = { PopulatedCategoriesRepositoryTest.class }, loader = AnnotationConfigContextLoader.class)
})
public class DeckServiceTest extends AbstractTransactionalServiceUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    CardService cardService;

    @Autowired
    DeckService deckService;

    private Integer category_id;


    @Test
    public void whenContextLoad_thenCorrect()
    {
        assertTrue(categoryRepository.count() > 0);

    }


    @Before
    public void givenExistingCategory()
    {
        Category category = findFirstCategory();
        category_id = category.getId();
    }

    @Transactional
    private Category findFirstCategory() {
        Category category = categoryRepository.findAll().iterator().next();
        return category;
    }

    @Test
    @DirtiesContext
    public void whenCreateDeck_thenCorrect() {

        Deck deck = new Deck();
        Category category = new Category();
        category.setId(category_id);
        deck.setCategory(category);

        deck = deckService.createDeck(deck);

        assertDeckRepositoryCount(1);
    }

    @Test
    @DirtiesContext
    public void whenCreateTwoDeck_thenCorrect() {

        Deck deck = new Deck();
        Category category = new Category();
        category.setId(category_id);
        deck.setCategory(category);

        deckService.createDeck(deck);

        deck = new Deck();
        category.setId(category_id);
        deck.setCategory(category);

        deckService.createDeck(deck);

        assertDeckRepositoryCount(2);
    }

    @Test
    @DirtiesContext
    public void whenCreatingDeckWithFlashCard_thenCorrect() {

        Deck deck = new Deck();
        Category category = new Category();
        category.setId(category_id);
        deck.setCategory(category);

        deck = deckService.createDeck(deck);

        FlashCard flashCard = new FlashCard(new Question("Que?"), new Answer(("Ans.")));
        deckService.addToDeckFlashCard(deck, flashCard);
    }


}

