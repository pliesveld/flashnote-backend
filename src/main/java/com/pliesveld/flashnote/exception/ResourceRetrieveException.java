package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Created by happs on 1/20/16.
 */
public abstract class ResourceRetrieveException extends RuntimeException {
    protected Serializable id;

    public ResourceRetrieveException(Serializable id) {
        super();
        this.id = id;
    }

    public Serializable getRepositoryId()
    {
        return id;
    }

    abstract public String getRepositoryMessage();
    abstract public HttpStatus getRepositoryStatus();

    public ResourceRetrieveException(String message) {
        super(message);
    }

    public ResourceRetrieveException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceRetrieveException(Throwable cause) {
        super(cause);
    }

    protected ResourceRetrieveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
