package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.repository.RepositoryCategoriesTest;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import com.pliesveld.flashnote.spring.SpringServiceTestConfig;
import com.pliesveld.tests.AbstractTransactionalRepositoryUnitTest;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(classes = { SpringDataTestConfig.class }, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = { SpringServiceTestConfig.class }, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = { RepositoryCategoriesTest.class }, loader = AnnotationConfigContextLoader.class)
})
public class DeckServiceTest extends AbstractTransactionalRepositoryUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    CardService cardService;

    @Autowired
    DeckService deckService;

    private Integer category_id;

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

