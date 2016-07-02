package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(name = "REPOSITORY", classes = {PopulatedDecksRepositoryTest.class}, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class DeckServiceTest extends AbstractTransactionalServiceUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    private DeckService deckService;

    private Category category = null;
    private static Integer category_id;

    @Test
    public void whenContextLoad_thenCorrect() {
        assertTrue(categoryRepository.count() > 0);
        assertTrue(deckRepository.count() > 0);
    }

    @Before
    public void givenExistingCategory() {
        if (category_id == null) {
            Category category = categoryRepository.findOneByNameEquals("TEST DECK CATEGORY");
            category_id = category.getId();
        }
        category = new Category();
        category.setId(category_id);
    }

    @Test
    public void whenCreateDeck_thenCorrect() {

        Deck deck = new Deck(category, UUID.randomUUID().toString());
        deck = deckService.createDeck(deck);
        assertNotNull(deck);
        assertNotNull(deck.getId());
    }

    @Test
    public void whenCreatingDeckWithFlashCard_thenCorrect() {

        Deck deck = new Deck(category, UUID.randomUUID().toString());
        deck = deckService.createDeck(deck);
        FlashCard flashCard = new FlashCard(new Question("Que?"), new Answer(("Ans.")));
        deckService.updateDeckAddFlashCard(deck.getId(), flashCard);
    }


    @Test
    public void whenCreatingDeckWithFlashCardCascade_thenCorrect() {

        Deck deck = new Deck(category, UUID.randomUUID().toString());
        FlashCard flashCard = new FlashCard(new Question("Que?"), new Answer(("Ans.")));
        deck.getFlashcards().add(flashCard);
        deck = deckService.createDeck(deck);
        assertNotNull(deck);
        assertNotNull(deck.getId());
    }

    @Test
    public void givenQuestion_whenCreatingDeckWithFlashCardByReferencingQuestion_thenCorrect() {
        Deck deck = new Deck(category, UUID.randomUUID().toString());
        deck = deckService.createDeck(deck);
        final Specification<Question> spec = QuestionSpecification.contentContainsIgnoreCase("EXISTINGQUESTION");
        final Question question = questionRepository.findOne(spec);
        assertNotNull(question);
        FlashCard flashCard = new FlashCard(question, new Answer(("Ans.")));
        deckService.updateDeckAddFlashCard(deck.getId(), flashCard);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void givenQuestion_whenCreatingDeckWithFlashCardByReference_thenCorrect() {

        Deck deck = new Deck(category, UUID.randomUUID().toString());
        deck = deckService.createDeck(deck);
        final Specification<Question> questionSpec = QuestionSpecification.contentContainsIgnoreCase("EXISTINGQUESTION");
        final Question question = questionRepository.findOne(questionSpec);
        assertNotNull(question);
        final Specification<Answer> answerSpec = AnswerSpecification.contentContainsIgnoreCase("EXISTINGANSWER");
        final Answer answer = answerRepository.findOne(answerSpec);
        assertNotNull(answer);
        FlashCard flashCard = new FlashCard(question, answer);
        deckService.updateDeckAddFlashCard(deck.getId(), flashCard);
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
        deckService.updateDeckAddFlashCard(deckId, flashCard);
    }

}

