package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

public class AttachmentDownloadException extends ResourceRepositoryException {

    @Override
    public String getRepositoryMessage() {
        return getMessage() != null ? getMessage() : "Incountered error while processing attachment for download";
    }

    @Override
    public HttpStatus getRepositoryStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public AttachmentDownloadException(String message) {
        super(message);
    }

    public AttachmentDownloadException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttachmentDownloadException(Throwable cause) {
        super(cause);
    }

    protected AttachmentDownloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
