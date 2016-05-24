package com.pliesveld.flashnote.exception;

import java.io.Serializable;


public class DeckNotFoundException extends ResourceNotFoundException {
    public DeckNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Deck not found: " + getRepositoryId();
    }
}
