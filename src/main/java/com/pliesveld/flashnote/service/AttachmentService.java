package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Attachment;
import com.pliesveld.flashnote.domain.dto.AttachmentHeader;
import com.pliesveld.flashnote.exception.AttachmentNotFoundException;
import com.pliesveld.flashnote.exception.AttachmentUploadException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.web.validator.ValidAttachment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
public interface AttachmentService {
    Attachment          findAttachmentById(int id)                        throws AttachmentNotFoundException;

    @Transactional
    Attachment storeAttachment(@ValidAttachment Attachment attachment)    throws AttachmentUploadException;

    @Transactional(rollbackFor = StudentNotFoundException.class)
    Attachment delete(int id)                                             throws AttachmentNotFoundException;

    void                removeAttachmentById(int id)                      throws AttachmentNotFoundException;
    List<Attachment>    findAttachmentByStudent(int id)                   throws StudentNotFoundException;

    AttachmentHeader findAttachmentHeaderById(int id)                     throws AttachmentNotFoundException;

    Integer testValidation(@NotNull Integer arg);
}
