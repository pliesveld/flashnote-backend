package com.pliesveld.flashnote.exception;

import java.io.Serializable;


public class AttachmentNotFoundException extends ResourceNotFoundException {
    public AttachmentNotFoundException(Serializable id) {
        super(id);
    }

    @Override
    public String getRepositoryMessage() {
        return "Attachment not found: " + getRepositoryId();
    }
}
