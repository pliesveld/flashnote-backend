package com.pliesveld.flashnote.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class StudentCreateException extends ResourceCreateException {

    private static final long serialVersionUID = -3530788942030825441L;

    public StudentCreateException(Serializable id) {
        super("Student resource " + id + " already exists.");
    }

    public StudentCreateException(String message) {
        super(message);
    }

    public StudentCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getRepositoryMessage() {
        return this.getMessage();
    }

    @Override
    public HttpStatus getRepositoryStatus() {
        return HttpStatus.CONFLICT;
    }


}
