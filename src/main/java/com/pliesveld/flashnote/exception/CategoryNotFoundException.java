package com.pliesveld.flashnote.exception;

import java.io.Serializable;

public class CategoryNotFoundException extends ResourceNotFoundException {

    private static final long serialVersionUID = 2203157108966280168L;

    public CategoryNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Category not found: " + getRepositoryId();
    }
}
