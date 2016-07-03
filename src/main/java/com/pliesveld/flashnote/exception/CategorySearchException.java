package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;


public class CategorySearchException extends ResourceRetrieveException {

    private static final long serialVersionUID = -5790732190706536001L;

    public CategorySearchException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Invalid query term";
    }

    @Override
    public HttpStatus getRepositoryStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
