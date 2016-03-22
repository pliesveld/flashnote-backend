package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public abstract class ResourceCreateException extends ResourceRepositoryException {

    @Override
    public HttpStatus getRepositoryStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    protected ResourceCreateException(String message) {
        super(message);
    }

    protected ResourceCreateException(String message, Throwable cause) {
        super(message, cause);
    }
}
