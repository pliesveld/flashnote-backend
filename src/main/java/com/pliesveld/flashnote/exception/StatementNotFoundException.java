package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class StatementNotFoundException extends ResourceNotFoundException {

    public StatementNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Statement not found: " + getRepositoryId();
    }

}
