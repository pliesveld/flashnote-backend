package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author Patrick Liesveld
 */
public abstract class ResourceRetrieveException extends ResourceRepositoryException {
    protected Serializable id;

    public ResourceRetrieveException(Serializable id) {
        super("Resource not found");
        this.id = id;
    }
    public Serializable getRepositoryId()
    {
        return id;
    }


    abstract public String getRepositoryMessage();
    abstract public HttpStatus getRepositoryStatus();


}
