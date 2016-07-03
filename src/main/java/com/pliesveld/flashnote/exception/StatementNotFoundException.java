package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class StatementNotFoundException extends ResourceNotFoundException {

    private static final long serialVersionUID = 7635629193157841403L;

    public StatementNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Statement not found: " + getRepositoryId();
    }

}
