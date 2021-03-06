package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "ATTACHMENT_BINARY")
@PrimaryKeyJoinColumn(name = "ATTACHMENT_ID", foreignKey = @ForeignKey(name = "FK_ATTACHMENT_BINARY"))
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, visible = false)
public class AttachmentBinary extends AbstractAttachment implements Serializable {

    private static final long serialVersionUID = -560331548564003003L;
    private byte[] contents;

    @NotNull
    @Size(max = Constants.MAX_ATTACHMENT_BINARY_FILE_LENGTH)
    @Column(name = "FILE_DATA", length = Constants.MAX_ATTACHMENT_BINARY_FILE_LENGTH, nullable = false)
    @Basic(fetch = FetchType.LAZY, optional = false)
    @JsonView(Views.SummaryDetails.class)
    public byte[] getContents() {
        return contents;
    }

    public void setContents(byte[] contents) {
        this.contents = contents;
        setFileLength(contents.length);
    }

    public AttachmentBinary() {
        super();
    }

    @PrePersist
    public void prePersist() {
        if (attachmentType == null)
            attachmentType = AttachmentType.BINARY;
    }

}
