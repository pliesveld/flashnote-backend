package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class StudentNotFoundException extends ResourceNotFoundException {

    private static final long serialVersionUID = 7729335038254854816L;

    public StudentNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Student id not found: " + getRepositoryId();
    }
}
