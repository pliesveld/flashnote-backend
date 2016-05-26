package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.repository.DeckRepository;
import com.pliesveld.flashnote.repository.specifications.DeckSpecification;
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

@Service("deckService")
public class DeckServiceImpl implements DeckService {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DeckRepository deckRepository;

    @Override
    public Deck findDeckById(final int id) throws DeckNotFoundException {
        Deck deck = deckRepository.findOne(id);
        if(deck == null)
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
    public Deck createDeck(final Deck deck) {
        return deckRepository.save(deck);
    }

    @Override
    public void deleteDeck(final int id) {
        if(!deckRepository.exists(id))
            throw new DeckNotFoundException(id);
        deckRepository.delete(id);
    }

}
