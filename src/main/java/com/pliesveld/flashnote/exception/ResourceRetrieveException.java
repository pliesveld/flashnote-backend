package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;


public abstract class ResourceRetrieveException extends ResourceRepositoryException {
    private static final long serialVersionUID = 5631791231193445091L;
    protected Serializable id;

    public ResourceRetrieveException(Serializable id) {
        super("Resource not found");
        this.id = id;
    }

    public Serializable getRepositoryId() {
        return id;
    }


    abstract public String getRepositoryMessage();

    abstract public HttpStatus getRepositoryStatus();


}
