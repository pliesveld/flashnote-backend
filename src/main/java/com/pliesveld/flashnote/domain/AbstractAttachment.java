package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
import com.pliesveld.flashnote.domain.converter.AttachmentTypeConverter;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ATTACHMENT")
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries(value = {
        @NamedQuery(name = "AbstractAttachment.findHeaderByAttachmentId",
                query = "SELECT NEW com.pliesveld.flashnote.domain.dto.AttachmentHeader(a.contentType, a.fileLength, a.modifiedOn) FROM AbstractAttachment a WHERE a.id = :id")
})
public abstract class AbstractAttachment extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ATTACHMENT_ID")
    Integer id;

    @Column(name = "CONTENT_TYPE", length = 16, nullable = false)   @NotNull
    @Convert(converter = AttachmentTypeConverter.class)
    AttachmentType contentType;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "STATEMENT_ID", referencedColumnName = "STATEMENT_ID",foreignKey = @ForeignKey(name = "FK_ATTACHMENT_STATEMENT"))
    AbstractStatement statement;

    @NotNull
    @Column(name = "FILENAME", length = Constants.MAX_ATTACHMENT_FILENAME_LENGTH, nullable = false)
    String fileName;

    @Column(name = "FILE_LENGTH")
    int fileLength;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AttachmentType getAttachmentType() {
        return contentType;
    }

    public String getMimeType() { return contentType.getMime(); }

    public void setContentType(AttachmentType contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getFileLength() { return fileLength; }

    protected void setFileLength(int fileLength) { this.fileLength = fileLength; }

    public AbstractStatement getStatement() { return statement; }

    public void setStatement(AbstractStatement statement) { this.statement = statement; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractAttachment that = (AbstractAttachment) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


}
