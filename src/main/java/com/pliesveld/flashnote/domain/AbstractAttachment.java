package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
import com.pliesveld.flashnote.domain.converter.AttachmentTypeConverter;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Entity
@Table(name = "ATTACHMENT")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractAttachment extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ATTACHMENT_ID")
    @Access(AccessType.PROPERTY)
    /* for lazy-loading of subclasses when using .findOne(id),
            use em.getReference() within transaction context for proxy-object
            to load assosciated entities.
            or use fetch in JPQL query.

            Move all annontations to property? including embedded entities?
     */
    Integer id;

    @NotNull
    @Column(name = "CONTENT_TYPE", length = 16, nullable = false)
    @Convert(converter = AttachmentTypeConverter.class)
    AttachmentType attachmentType;

    @NotNull
    @Column(name = "FILENAME", length = Constants.MAX_ATTACHMENT_FILENAME_LENGTH, nullable = false)
    String fileName;

    @Column(name = "FILE_LENGTH")
    int fileLength;

    public abstract void setContents(byte[] contents) throws UnsupportedEncodingException;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public AttachmentType getAttachmentType() {
        return attachmentType;
    }
    public void setAttachmentType(AttachmentType attachmentType) { this.attachmentType = attachmentType; }

    public String getMimeContentType() { return attachmentType.getMime(); }

    public String getFileName() { return fileName; }

    public void setFileName(String fileName) { this.fileName = fileName; }

    public int getFileLength() { return fileLength; }
    protected void setFileLength(int fileLength) { this.fileLength = fileLength; }

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
