package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

public class ResourceLimitException extends RuntimeException {

    public String getRepositoryMessage()
    {
        return this.getMessage();
    }

    public HttpStatus getRepositoryStatus()
    {
        return HttpStatus.BANDWIDTH_LIMIT_EXCEEDED;
    }

    public ResourceLimitException(String message) {
        super(message);
    }

    public ResourceLimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceLimitException(Throwable cause) {
        super(cause);
    }

    protected ResourceLimitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
