package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class QuestionNotFoundException extends ResourceNotFoundException {

    public QuestionNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Question not found: " + getRepositoryId();
    }

}
