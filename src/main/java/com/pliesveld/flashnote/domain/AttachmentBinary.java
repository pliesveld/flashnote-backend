package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "ATTACHMENT_BINARY")
@PrimaryKeyJoinColumn(name = "ATTACHMENT_ID", foreignKey = @ForeignKey(name = "FK_ATTACHMENT_BINARY"))
public class AttachmentBinary extends AbstractAttachment implements Serializable {
    public AttachmentBinary() {
        super();
    }

    @NotNull
    @Size(max = Constants.MAX_ATTACHMENT_BINARY_FILE_LENGTH)
    @Column(name = "FILE_DATA", length = Constants.MAX_ATTACHMENT_BINARY_FILE_LENGTH, nullable = false)
    byte[] contents;

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
        setFileLength(contents.length);
    }

    @PrePersist
    public void prePersist()
    {
        if(attachmentType == null)
            attachmentType = AttachmentType.BINARY;
    }

}
