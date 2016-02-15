package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.exception.DeckNotFoundException;

public interface PlaybackService {
    void print(Deck deck) throws DeckNotFoundException;
}