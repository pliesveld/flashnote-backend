package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Created by happs on 1/20/16.
 */
public abstract class ResourceNotFoundException extends ResourceRepositoryException {
    private static final HttpStatus EXCEPTION_STATUS_NOT_FOUND = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(Serializable id) {
        super(id);
    }

    public HttpStatus getRepositoryStatus()
    {
        return EXCEPTION_STATUS_NOT_FOUND;
    }
}
