package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

public class SpeechException extends ResourceRepositoryException {

    @Override
    public String getRepositoryMessage() {
        return getMessage() != null ? getMessage() : "Encountered error while processing speech";
    }

    @Override
    public HttpStatus getRepositoryStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public SpeechException(String message) {
        super(message);
    }

    public SpeechException(String message, Throwable cause) {
        super(message, cause);
    }

    public SpeechException(Throwable cause) {
        super(cause);
    }

    protected SpeechException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
