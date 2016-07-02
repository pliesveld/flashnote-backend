package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class FlashCardCreateException extends ResourceCreateException {

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
