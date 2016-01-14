package com.pliesveld.flashnote.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="ATTACHMENT")
public class Attachment implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "ATTACHMENT_ID")
    Integer id;

    @Column(name="CONTENT_TYPE",length=16)
    @Convert(converter=AttachmentTypeConverter.class)
    AttachmentType contentType;

    @Column(name="FILENAME", length=48)
    String fileName;

    @Column(name="FILE_DATA")
    byte[] fileData;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AttachmentType getContentType() {
        return contentType;
    }

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
    }

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
