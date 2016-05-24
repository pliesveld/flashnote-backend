package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @author Patrick Liesveld
 */
public class ResourceNotFoundException extends ResourceRetrieveException {

    public ResourceNotFoundException(Serializable id) {
        super(id);
    }

    public HttpStatus getRepositoryStatus() { return HttpStatus.NOT_FOUND; }

    @Override
    public String getRepositoryMessage() {
        return "Resource " + id + " not found";
    }
}
