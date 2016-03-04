package com.pliesveld.flashnote.domain.dto;

import com.pliesveld.flashnote.domain.AttachmentType;

import java.time.Instant;

/**
 * DTO for AttachmentBinary entity.  Used in JPL named queries to retrieve header information of attachment
 */
public class AttachmentHeader {
    AttachmentType contentType;
    int length;
    Instant modified;

    public AttachmentHeader(AttachmentType contentType, int length, Instant modified) {
        this.contentType = contentType;
        this.length = length;
        this.modified = modified;
    }

    public AttachmentType getContentType() {
        return contentType;
    }

    public int getLength() {
        return length;
    }

    public Instant getModified() {
        return modified;
    }
}
