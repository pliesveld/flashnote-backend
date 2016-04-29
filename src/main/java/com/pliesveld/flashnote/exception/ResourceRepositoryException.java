package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

/**
 * Created by happs on 1/20/16.
 */
public abstract class ResourceRepositoryException extends RuntimeException {

    abstract public String getRepositoryMessage();
    abstract public HttpStatus getRepositoryStatus();

    public ResourceRepositoryException(String message) {
        super(message);
    }

    public ResourceRepositoryException(String message, Throwable cause) {
        this(message, cause, true, false);
    }

    public ResourceRepositoryException(Throwable cause) {
        super(cause);
    }

    protected ResourceRepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
