package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class QuestionBankNotFoundException extends ResourceNotFoundException {
    public QuestionBankNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "QuestionBank not found: " + getRepositoryId();
    }
}
