package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.schema.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

@Entity
@Table(name = "ATTACHMENT_TEXT")
@PrimaryKeyJoinColumn(name = "ATTACHMENT_ID", foreignKey = @ForeignKey(name = "FK_ATTACHMENT_TEXT"))
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE, visible = false)
public class AttachmentText extends AbstractAttachment implements Serializable {

    private static final long serialVersionUID = 2198784599569918824L;
    private String contents;

    @NotNull
    @Size(max = Constants.MAX_ATTACHMENT_TEXT_FILE_LENGTH)
    @Column(name = "FILE_TEXT", length = Constants.MAX_ATTACHMENT_TEXT_FILE_LENGTH, nullable = false)
    @JsonView(Views.SummaryDetails.class)
    public String getContents() {
        return contents;
    }

    @JsonSetter
    public void setContents(String contents) {
        this.contents = contents;
        setFileLength(contents.length());
    }

    @Override
    public void setContents(byte[] contents) throws UnsupportedEncodingException {
        CharsetDecoder cs = Charset.forName("UTF-8").newDecoder();
        this.contents = new String(contents, StringUtils.toEncodedString(contents, Charset.forName("UTF-8")));
    }

    public AttachmentText() {
        super();
    }

    @PrePersist
    public void prePersist() {
        if (attachmentType == null)
            attachmentType = AttachmentType.DOCUMENT_TEXT;
    }

}
