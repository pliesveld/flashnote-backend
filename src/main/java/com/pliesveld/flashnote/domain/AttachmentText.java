package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "ATTACHMENT_TEXT")
@PrimaryKeyJoinColumn(name = "ATTACHMENT_ID", foreignKey = @ForeignKey(name = "FK_ATTACHMENT_TEXT"))
public class AttachmentText extends AbstractAttachment {
    @NotNull
    @Column(name = "FILE_TEXT", length = Constants.MAX_ATTACHMENT_TEXT_FILE_LENGTH)
    @Size(max = Constants.MAX_ATTACHMENT_TEXT_FILE_LENGTH)
    String contents;

    public AttachmentText() {}

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
