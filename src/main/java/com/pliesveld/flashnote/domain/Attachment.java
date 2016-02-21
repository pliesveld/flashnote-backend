package com.pliesveld.flashnote.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name="ATTACHMENT")
@NamedQueries(value = {
        @NamedQuery(name = "Attachment.findHeaderByAttachmentId",
                query = "SELECT NEW com.pliesveld.flashnote.domain.AttachmentHeader(a.contentType, a.fileLength, a.modifiedOn) FROM Attachment a WHERE a.id = :id")
})
public class Attachment extends AbstractDatedEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ATTACHMENT_ID")
    Integer id;

    @Column(name = "CONTENT_TYPE",length=16)    @NotNull
    @Convert(converter=AttachmentTypeConverter.class)
    AttachmentType contentType;

    @Column(name = "FILENAME", length=48)       @NotNull
    String fileName;

    @Column(name = "FILE_DATA", length=3145728) @NotNull
    byte[] fileData;

    @Column(name = "FILE_LENGTH")
    int fileLength;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AttachmentType getContentType() {
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

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
        setFileLength(fileData.length);
    }

    public int getFileLength() { return fileLength; }

    private void setFileLength(int fileLength) { this.fileLength = fileLength; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attachment that = (Attachment) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }



}
