package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Validated
@Transactional(readOnly = true)
public interface DeckService {
    Deck findDeckById(final int id) throws DeckNotFoundException;

    List<Deck> findAllDecks();

    Page<Deck> findBySearchTerm(final String searchTerm, final Pageable pageRequest);

    Page<Deck> browseDecks(final Pageable pageRequest);

    Page<Deck> browseDecksWithSpec(final Specification<Deck> specification, final Pageable pageRequest);

    Page<Deck> findByCategory(final Integer id, final Pageable pageRequest);

    @Deprecated
    @Transactional
    void addToDeckFlashCard(final Deck deck, final FlashCard flashCard);

    @Transactional
    void updateDeckAddFlashCard(final int deckId, final FlashCard flashCard) throws DeckNotFoundException;

    @Transactional
    Deck createDeck(final Deck deck);

    @Transactional
    Deck updateDeck(@Valid final Deck deck);

    @Transactional
    void deleteDeck(final int id);


}
