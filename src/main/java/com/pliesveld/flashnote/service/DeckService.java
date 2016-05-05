package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

/**
 * Created by patrick on 5/5/16.
 */
@Validated
@Transactional(readOnly = true)
public interface DeckService {
    Deck findDeckById(int id) throws DeckNotFoundException;

    void addToDeckFlashCard(Deck deck, FlashCard flashCard);

    List<Deck> findAllDecks();

    Deck createDeck(Deck deck);

    void deleteDeck(int id);

    Page<Deck> findBySearchTerm(String searchTerm, Pageable pageRequest);
}
