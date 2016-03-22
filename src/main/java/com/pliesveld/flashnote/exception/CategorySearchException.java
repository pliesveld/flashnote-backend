package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Created by happs on 2/22/16.
 */
public class CategorySearchException extends ResourceRetrieveException {

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
