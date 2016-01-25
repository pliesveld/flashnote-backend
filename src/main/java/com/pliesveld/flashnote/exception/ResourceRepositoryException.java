package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Created by happs on 1/20/16.
 */
public abstract class ResourceRepositoryException extends RuntimeException {
    protected Serializable id;

    public ResourceRepositoryException(Serializable id) {
        super();
        this.id = id;
    }

    public Serializable getRepositoryId()
    {
        return id;
    }

    abstract public String getRepositoryMessage();
    abstract public HttpStatus getRepositoryStatus();

    public ResourceRepositoryException(String message) {
        super(message);
    }

    public ResourceRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceRepositoryException(Throwable cause) {
        super(cause);
    }

    protected ResourceRepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
