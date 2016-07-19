package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;


public class AttachmentNotSupportedException extends ResourceRepositoryException {
    private static final long serialVersionUID = -4904169351125161553L;

    @Override
    public String getRepositoryMessage() {
        return getMessage() != null ? getMessage() : "Encountered error while processing attachment";
    }

    @Override
    public HttpStatus getRepositoryStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public AttachmentNotSupportedException(String message) {
        super(message);
    }

    public AttachmentNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttachmentNotSupportedException(Throwable cause) {
        super(cause);
    }

    protected AttachmentNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

