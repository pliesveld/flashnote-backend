package com.pliesveld.flashnote.exception;

import java.io.Serializable;

/**
 * Created by happs on 1/20/16.
 */
public class DeckNotFoundException extends ResourceNotFoundException {
    public DeckNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Deck not found: " + getRepositoryId();
    }
}
