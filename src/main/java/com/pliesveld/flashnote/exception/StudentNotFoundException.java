package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class StudentNotFoundException extends ResourceNotFoundException {

    public StudentNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Student id not found: " + getRepositoryId();
    }
}
