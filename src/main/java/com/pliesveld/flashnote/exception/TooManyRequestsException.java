package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TooManyRequestsException extends ResourceRepositoryException {
    private static final long serialVersionUID = 7759869897709162318L;
    final String dev_message;

    public TooManyRequestsException(int secondsToWait) {
        super("Must wait " + secondsToWait);
        dev_message = Instant.now().plus(secondsToWait, ChronoUnit.SECONDS).toString();
    }

    @Override
    public String getRepositoryMessage() {
        return dev_message;
    }

    @Override
    public HttpStatus getRepositoryStatus() {
        return HttpStatus.BANDWIDTH_LIMIT_EXCEEDED;
    }
}
