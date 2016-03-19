package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Created by happs on 1/20/16.
 */
public abstract class ResourceNotFoundException extends ResourceRetrieveException {

    public ResourceNotFoundException(Serializable id) {
        super(id);
    }

    public HttpStatus getRepositoryStatus() { return HttpStatus.NOT_FOUND; }
}
