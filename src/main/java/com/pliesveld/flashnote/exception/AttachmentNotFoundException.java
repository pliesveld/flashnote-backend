package com.pliesveld.flashnote.exception;

import java.io.Serializable;


public class AttachmentNotFoundException extends ResourceNotFoundException {
    private static final long serialVersionUID = -4900169051185161553L;

    public AttachmentNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Attachment not found: " + getRepositoryId();
    }
}
