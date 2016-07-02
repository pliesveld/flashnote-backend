package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.FlashCardPrimaryKey;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.repository.DeckRepository;
import com.pliesveld.flashnote.repository.FlashCardRepository;
import com.pliesveld.flashnote.repository.QuestionBankRepository;
import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.flashnote.repository.specifications.DeckSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service("deckService")
public class DeckServiceImpl implements DeckService {
    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private FlashCardRepository flashcardRepository;

    @Override
    public Deck findDeckById(final int id) throws DeckNotFoundException {
        Deck deck = deckRepository.findOne(id);
        if (deck == null)
            throw new DeckNotFoundException(id);

        Hibernate.initialize(deck.getFlashcards());
        deck.getCategory().getId();
        deck.getDescription();
        return deck;
    }

    @Override
    public Page<Deck> findBySearchTerm(final String searchTerm, final Pageable pageRequest) {
        final Specification<Deck> spec = DeckSpecification.descriptionOrFlashcardContainsIgnoreCase(searchTerm);
        return deckRepository.findAll(spec, pageRequest);
    }

    @Override
    public Page<Deck> browseDecks(final Pageable pageRequest) {
        return deckRepository.findAll(pageRequest);
    }

    @Override
    public Page<Deck> browseDecksWithSpec(Specification<Deck> specification, Pageable pageRequest) {
        return deckRepository.findAll(specification, pageRequest);
    }

    @Override
    public List<Deck> findAllDecks() {
        List<Deck> list = new ArrayList<>();
        deckRepository.findAll().forEach(deck -> {
            deck.getFlashcards().forEach(flashcard -> {
                flashcard.getId();
                flashcard.getQuestion().getId();
                flashcard.getAnswer().getId();
            });
            //Hibernate.initialize(flashcard.getFlashcards());
        });
        return list;
    }

    @Override
    public void addToDeckFlashCard(Deck deck, FlashCard flashCard) {
        deck = entityManager.merge(deck);
        flashCard = entityManager.merge(flashCard);
        deck.getFlashcards().add(flashCard);
    }

    @Override
    public void updateDeckAddFlashCard(final int deckId, FlashCard flashCard) {
        final Deck deck = findDeckById(deckId);
        if (deck == null)
            throw new DeckNotFoundException(deckId);

        if (LOG.isDebugEnabled()) {
            LOG.debug("Adding fc: {}", flashCard.getId());
            LOG.debug("Checking Deck:");
            deck.getFlashcards().forEach(fc -> LOG.debug("Has {}", fc.getId()));
        }

        if (flashCard.getId() != null) {
            final Integer questionId = flashCard.getId().getQuestionId();
            final Integer answerId = flashCard.getId().getAnswerId();

            if (questionId != null) {
                flashCard = entityManager.merge(flashCard);
            }

            final FlashCardPrimaryKey flashcardId = flashCard.getId();
            if (deck.getFlashcards().stream().map(FlashCard::getId).anyMatch(fc -> fc.equals(flashcardId))) {
                throw new FlashCardCreateException(flashCard.getId());
            }
        }

        deck.getFlashcards().add(flashCard);
    }

    @Override
    public Deck createDeck(final Deck deck) {
        return deckRepository.save(deck);
    }

    /**
     * Deletes deck specified by id.
     * Iterates flashcards to find any orphaned Question entities and removes them if no other QuestionBank
     * or Deck contains a reference to the Question.
     * @param id
     */
    @Override
    public void deleteDeck(final int id) {

        if (!deckRepository.exists(id))
            throw new DeckNotFoundException(id);

        final Deck deck = deckRepository.findOne(id);
        final List<FlashCard> flashCards = deck.getFlashcards().stream().collect(Collectors.toList());
        final List<Integer> orphanQuestions = new ArrayList<>();

        for (FlashCard flashcard : flashCards) {
            final FlashCardPrimaryKey flashcardId = flashcard.getId();

            if ( flashcardRepository.findAllByQuestion_id(flashcardId.getQuestionId()).stream().filter( fc -> !flashCards.contains(fc)).count() > 0)
            {
                continue;
            }

            if ( questionBankRepository.findByQuestionsContaining(flashcard.getQuestion()).size() > 0)
            {
                continue;
            }

            orphanQuestions.add(flashcard.getId().getQuestionId());
        }

        deckRepository.delete(deck);
        orphanQuestions.forEach(questionRepository::delete);
    }

    @Override
    public Page<Deck> findByCategory(Integer id, Pageable pageRequest) {
        final Specification<Deck> spec = DeckSpecification.hasCategory(id);
        return deckRepository.findAll(spec, pageRequest);

    }
}
