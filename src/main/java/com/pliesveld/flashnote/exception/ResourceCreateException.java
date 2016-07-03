package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

public abstract class ResourceCreateException extends ResourceRepositoryException {

    private static final long serialVersionUID = 5245022373764555519L;

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
