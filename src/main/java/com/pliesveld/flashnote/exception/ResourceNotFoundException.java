package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;


public class ResourceNotFoundException extends ResourceRetrieveException {

    private static final long serialVersionUID = -3025965932881136668L;

    public ResourceNotFoundException(Serializable id) {
        super(id);
    }

    public HttpStatus getRepositoryStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getRepositoryMessage() {
        return "Resource " + id + " not found";
    }
}
