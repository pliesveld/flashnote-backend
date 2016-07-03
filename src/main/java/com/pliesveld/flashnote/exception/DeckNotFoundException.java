package com.pliesveld.flashnote.exception;

import java.io.Serializable;


public class DeckNotFoundException extends ResourceNotFoundException {
    private static final long serialVersionUID = 8067162658036596100L;

    public DeckNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Deck not found: " + getRepositoryId();
    }
}
