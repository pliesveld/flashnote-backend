package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.schema.Constants;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

@Entity
@Table(name = "ATTACHMENT_TEXT")
@PrimaryKeyJoinColumn(name = "ATTACHMENT_ID", foreignKey = @ForeignKey(name = "FK_ATTACHMENT_TEXT"))
public class AttachmentText extends AbstractAttachment {
    @NotNull
    @Column(name = "FILE_TEXT", length = Constants.MAX_ATTACHMENT_TEXT_FILE_LENGTH, nullable = false)
    @Size(max = Constants.MAX_ATTACHMENT_TEXT_FILE_LENGTH)
    String contents;

    public AttachmentText() {
        super();
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
        setFileLength(contents.length());
    }

    @Override
    public void setContents(byte[] contents) throws UnsupportedEncodingException {
        CharsetDecoder cs = Charset.forName("UTF-8").newDecoder();
        this.contents = new String(contents, StringUtils.toEncodedString(contents, Charset.forName("UTF-8")));
    }

    @PrePersist
    public void prePersist()
    {
        if(attachmentType == null)
            attachmentType = AttachmentType.TEXT;
    }

}
