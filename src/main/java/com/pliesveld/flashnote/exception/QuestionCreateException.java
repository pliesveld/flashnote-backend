package com.pliesveld.flashnote.exception;

public class QuestionCreateException extends ResourceCreateException {

    private static final long serialVersionUID = -2784981318138399311L;

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
