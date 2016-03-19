package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class AnswerNotFoundException extends ResourceNotFoundException {

    public AnswerNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Answer not found: " + getRepositoryId();
    }

}
