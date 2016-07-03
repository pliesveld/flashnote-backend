package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class AnswerNotFoundException extends StatementNotFoundException {

    private static final long serialVersionUID = -1029003813629083879L;

    public AnswerNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Answer not found: " + getRepositoryId();
    }

}
