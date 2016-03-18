package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

public class AttachmentUploadException extends ResourceCreateException {

    public AttachmentUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttachmentUploadException(String message) {
        super(message);
    }

    @Override
    public String getRepositoryMessage() {
        return super.getMessage();
    }

    @Override
    public HttpStatus getRepositoryStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
