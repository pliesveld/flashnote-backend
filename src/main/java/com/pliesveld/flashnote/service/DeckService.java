package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Transactional(readOnly = true)
public interface DeckService {
    Deck findDeckById(final int id) throws DeckNotFoundException;

    List<Deck> findAllDecks();

    Page<Deck> findBySearchTerm(final String searchTerm, final Pageable pageRequest);

    Page<Deck> browseDecks(final Pageable pageRequest);

    @Transactional
    void addToDeckFlashCard(final Deck deck, final FlashCard flashCard);

    @Transactional
    Deck createDeck(final Deck deck);

    @Transactional
    void deleteDeck(final int id);


}
