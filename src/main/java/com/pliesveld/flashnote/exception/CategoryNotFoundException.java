package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class CategoryNotFoundException extends ResourceNotFoundException {

    public CategoryNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Category not found: " + getRepositoryId();
    }
}
