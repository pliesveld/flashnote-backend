package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "ATTACHMENT_BINARY")
@PrimaryKeyJoinColumn(name = "ATTACHMENT_ID", foreignKey = @ForeignKey(name = "FK_ATTACHMENT_BINARY"))
public class AttachmentBinary extends AbstractAttachment implements Serializable {
    public AttachmentBinary() {}

    @NotNull
    @Column(name = "FILE_DATA", length = Constants.MAX_ATTACHMENT_BINARY_FILE_LENGTH)
    byte[] contents;

    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
        setFileLength(contents.length);
    }

}
