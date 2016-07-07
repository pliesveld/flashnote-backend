package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
import com.pliesveld.flashnote.domain.converter.AttachmentTypeConverter;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Entity
@Table(name = "ATTACHMENT")
@Inheritance(strategy = InheritanceType.JOINED)
//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class", visible = false)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
public abstract class AbstractAttachment extends AbstractAuditableEntity<Integer> implements Serializable {

    private static final long serialVersionUID = 1475231744214090102L;
    protected Integer id;
    protected AttachmentType attachmentType;
    protected String fileName;
    protected int fileLength;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ATTACHMENT_ID")
    @JsonView(Views.Summary.class)
    public Integer getId() {
        return id;
    }

    @NotNull
    @Column(name = "CONTENT_TYPE", length = 16, nullable = false)
    @Convert(converter = AttachmentTypeConverter.class)
    @JsonView(Views.Summary.class)
    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    @NotNull
    @Size(min = Constants.MIN_ATTACHMENT_FILENAME_LENGTH, max = Constants.MAX_ATTACHMENT_FILENAME_LENGTH)
    @Column(name = "FILENAME", length = Constants.MAX_ATTACHMENT_FILENAME_LENGTH, nullable = false)
    @JsonView(Views.Summary.class)
    public String getFileName() {
        return fileName;
    }

    @NotNull
    @Column(name = "FILE_LENGTH", nullable = false)
    @JsonView(Views.Summary.class)
    public int getFileLength() {
        return fileLength;
    }

    protected AbstractAttachment() {
        super();
    }

    public abstract void setContents(byte[] contents) throws UnsupportedEncodingException;


    public void setId(Integer id) {
        this.id = id;
    }

    public void setAttachmentType(AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    @Transient
    public String getMimeContentType() {
        return attachmentType.getMime();
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    protected void setFileLength(int fileLength) {
        this.fileLength = fileLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof AbstractAttachment)) {
            return false;
        }
        final AbstractAttachment other = (AbstractAttachment) obj;
        return Objects.equals(getFileName(), other.getFileName());
    }
}
