package com.pliesveld.flashnote.model.json.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pliesveld.flashnote.domain.AttachmentType;
import com.pliesveld.flashnote.model.json.base.JsonWebResponseSerializable;
import com.pliesveld.flashnote.model.json.base.ModelBase;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * DTO for AttachmentBinary entity.  Used in JPL named queries to retrieve header information of attachment
 */
@Component
@JsonInclude(JsonInclude.Include.ALWAYS)
public class AttachmentHeader extends ModelBase implements JsonWebResponseSerializable {
    AttachmentType contentType;
    int length;
    Instant modified;

    public AttachmentHeader() {}

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

    public void setContentType(AttachmentType contentType) { this.contentType = contentType; }

    public void setLength(int length) { this.length = length; }

    public void setModified(Instant modified) { this.modified = modified; }
}