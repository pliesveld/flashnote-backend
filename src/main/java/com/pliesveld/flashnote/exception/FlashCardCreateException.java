package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class FlashCardCreateException extends ResourceCreateException {

    private static final long serialVersionUID = -3457444872126345846L;

    @Override
    public String getRepositoryMessage() {
        return super.getMessage();
    }

    public FlashCardCreateException(Serializable id) {
        super("FlashCard " + id + " already exists");
    }


    public FlashCardCreateException(String message) {
        super(message);
    }

    public FlashCardCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
