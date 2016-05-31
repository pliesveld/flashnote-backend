package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.repository.PopulatedCategoriesRepositoryTest;
import com.pliesveld.flashnote.repository.PopulatedDecksRepositoryTest;

import com.pliesveld.flashnote.repository.specifications.AnswerSpecification;
import com.pliesveld.flashnote.repository.specifications.DeckSpecification;
import com.pliesveld.flashnote.repository.specifications.QuestionSpecification;
import com.pliesveld.flashnote.spring.Profiles;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(name = "REPOSITORY", classes = { PopulatedDecksRepositoryTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class DeckServiceTest extends AbstractTransactionalServiceUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private DeckService deckService;

    private static Integer category_id;

    @Test
    public void whenContextLoad_thenCorrect()
    {
        assertTrue(categoryRepository.count() > 0);
        assertTrue(deckRepository.count() > 0);
    }

    @Before
    public void givenExistingCategory()
    {
        if( category_id == null )
        {
            Category category = categoryRepository.findOneByNameEquals("TEST DECK CATEGORY");
            category_id = category.getId();
        }
    }

    @Test
    public void whenCreateDeck_thenCorrect() {

        Deck deck = new Deck(UUID.randomUUID().toString());
        Category category = new Category();
        category.setId(category_id);
        deck.setCategory(category);
        deck = deckService.createDeck(deck);
        assertNotNull(deck);
        assertNotNull(deck.getId());
    }

    @Test
    public void whenCreatingDeckWithFlashCard_thenCorrect() {

        Deck deck = new Deck(UUID.randomUUID().toString());
        Category category = new Category();
        category.setId(category_id);
        deck.setCategory(category);
        deck = deckService.createDeck(deck);
        FlashCard flashCard = new FlashCard(new Question("Que?"), new Answer(("Ans.")));
        deckService.addToDeckFlashCard(deck, flashCard);
    }


    @Test
    public void whenCreatingDeckWithFlashCardCascade_thenCorrect() {

        Deck deck = new Deck(UUID.randomUUID().toString());
        Category category = new Category();
        category.setId(category_id);
        deck.setCategory(category);

        FlashCard flashCard = new FlashCard(new Question("Que?"), new Answer(("Ans.")));
        deck.getFlashcards().add(flashCard);

        deck = deckService.createDeck(deck);

        assertNotNull(deck);
        assertNotNull(deck.getId());
    }


    @Test
    public void givenQuestion_whenCreatingDeckWithFlashCardByReferencingQuestion_thenCorrect() {
        Deck deck = new Deck(UUID.randomUUID().toString());
        Category category = new Category();
        category.setId(category_id);
        deck.setCategory(category);
        deck = deckService.createDeck(deck);
        final Specification<Question> spec = QuestionSpecification.contentContainsIgnoreCase("EXISTINGQUESTION");
        final Question question = questionRepository.findOne(spec);
        assertNotNull(question);
        FlashCard flashCard = new FlashCard(question, new Answer(("Ans.")));
        deckService.addToDeckFlashCard(deck, flashCard);
    }


    @Test
    public void givenAnswer_whenCreatingDeckWithFlashCardByReferenceAnswer_thenCorrect() {
        Deck deck = new Deck(UUID.randomUUID().toString());
        Category category = new Category();
        category.setId(category_id);
        deck.setCategory(category);
        deck = deckService.createDeck(deck);
        final Specification<Answer> answerSpec = AnswerSpecification.contentContainsIgnoreCase("EXISTINGANSWER");
        final Answer answer = answerRepository.findOne(answerSpec);
        assertNotNull(answer);
        FlashCard flashCard = new FlashCard(new Question("Que?"), answer);
        deckService.addToDeckFlashCard(deck, flashCard);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenQuestion_whenCreatingDeckWithFlashCardByReference_thenCorrect() {

        Deck deck = new Deck(UUID.randomUUID().toString());
        Category category = new Category();
        category.setId(category_id);
        deck.setCategory(category);
        deck = deckService.createDeck(deck);
        final Specification<Question> questionSpec = QuestionSpecification.contentContainsIgnoreCase("EXISTINGQUESTION");
        final Question question = questionRepository.findOne(questionSpec);
        assertNotNull(question);
        final Specification<Answer> answerSpec = AnswerSpecification.contentContainsIgnoreCase("EXISTINGANSWER");
        final Answer answer = answerRepository.findOne(answerSpec);
        assertNotNull(answer);
        FlashCard flashCard = new FlashCard(question, answer);
        deckService.addToDeckFlashCard(deck, flashCard);
    }


    @Test
    public void givenExistingDeck_whenFind_thenCorrect() {

        final Specification<Deck> deckSpec = DeckSpecification.descriptionOrFlashcardContainsIgnoreCase("EXISTINGDECK");

        Deck deck = deckRepository.findOne(deckSpec);
        assertNotNull(deck);
        assertNotNull(deck.getId());

        final int deckId = deck.getId();

        deck = deckService.findDeckById(deckId);


        final Specification<Question> questionSpec = QuestionSpecification.contentContainsIgnoreCase("EXISTINGQUESTION");
        final Question question = questionRepository.findOne(questionSpec);
        assertNotNull(question);
        assertNotNull(question.getId());

        final Specification<Answer> answerSpec = AnswerSpecification.contentContainsIgnoreCase("EXISTINGANSWER");
        final Answer answer = answerRepository.findOne(answerSpec);
        assertNotNull(answer);
        assertNotNull(answer.getId());

        List<FlashCard> flashcards = deck.getFlashcards();

        assertNotNull(flashcards);

        assertTrue(flashcards.stream().map(FlashCard::getId).map(FlashCardPrimaryKey::getQuestionId).anyMatch(id -> id.equals(question.getId())));
        assertTrue(flashcards.stream().map(FlashCard::getId).map(FlashCardPrimaryKey::getAnswerId).anyMatch(id -> id.equals(answer.getId())));
    }


    @Test(expected = FlashCardCreateException.class)
    public void givenExistingDeck_whenAddingExistingFlashCard_thenCorrect() {

        final Specification<Deck> deckSpec = DeckSpecification.descriptionOrFlashcardContainsIgnoreCase("EXISTINGDECK");

        Deck deck = deckRepository.findOne(deckSpec);
        assertNotNull(deck);
        assertNotNull(deck.getId());

        final int deckId = deck.getId();

        final Specification<Question> questionSpec = QuestionSpecification.contentContainsIgnoreCase("EXISTINGQUESTION");
        final Question question = questionRepository.findOne(questionSpec);
        assertNotNull(question);
        assertNotNull(question.getId());

        final Specification<Answer> answerSpec = AnswerSpecification.contentContainsIgnoreCase("EXISTINGANSWER");
        final Answer answer = answerRepository.findOne(answerSpec);
        assertNotNull(answer);
        assertNotNull(answer.getId());

        FlashCard flashCard = new FlashCard(question, answer);
        deckService.addToDeckFlashCard(deckId,flashCard);

    }


}

