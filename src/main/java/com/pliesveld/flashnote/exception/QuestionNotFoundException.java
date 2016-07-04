package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class QuestionNotFoundException extends StatementNotFoundException {

    private static final long serialVersionUID = -2617090581173187458L;

    public QuestionNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Question not found: " + getRepositoryId();
    }

}
