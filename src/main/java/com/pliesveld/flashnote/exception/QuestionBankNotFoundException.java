package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class QuestionBankNotFoundException extends ResourceNotFoundException {
    private static final long serialVersionUID = -2120369880098608450L;

    public QuestionBankNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "QuestionBank not found: " + getRepositoryId();
    }
}
