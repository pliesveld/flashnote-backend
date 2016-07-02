package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class QuestionCreateException extends ResourceCreateException {

    public QuestionCreateException() {
        this("Could not create Question");
    }

    public QuestionCreateException(String message) {
        super(message);
    }

    public QuestionCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getRepositoryMessage() {
        return this.getMessage();
    }
}
