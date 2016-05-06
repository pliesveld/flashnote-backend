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
    Deck findDeckById(int id) throws DeckNotFoundException;

    List<Deck> findAllDecks();

    Page<Deck> findBySearchTerm(String searchTerm, Pageable pageRequest);

    Page<Deck> browseDecks(Pageable pageRequest);

    @Transactional
    void addToDeckFlashCard(Deck deck, FlashCard flashCard);

    @Transactional
    Deck createDeck(Deck deck);

    @Transactional
    void deleteDeck(int id);


}
