package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AbstractAttachment;
import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.domain.AttachmentText;
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
    AbstractAttachment findAttachmentById(int id)                        throws AttachmentNotFoundException;

    @Transactional
    AttachmentBinary storeAttachment(@ValidAttachment AttachmentBinary attachment) throws AttachmentUploadException;

    @Transactional
    AttachmentText storeAttachment(@ValidAttachment AttachmentText attachment)    throws AttachmentUploadException;

    @Transactional(rollbackFor = AttachmentNotFoundException.class)
    AbstractAttachment delete(int id)                                             throws AttachmentNotFoundException;

    @Transactional
    void                        removeAttachmentById(int id)                      throws AttachmentNotFoundException;
    @Transactional(readOnly = true)
    List<AbstractAttachment>    findAttachmentByStudent(int id)                   throws StudentNotFoundException;

    List<AttachmentBinary> findBinaryAttachmentByStudentEmail(String email) throws StudentNotFoundException;

    List<AttachmentText> findTextAttachmentByStudentEmail(String email) throws StudentNotFoundException;

    @Transactional(readOnly = true)
    List<AttachmentBinary>      findBinaryAttachmentByStudent(int id)             throws StudentNotFoundException;
    @Transactional(readOnly = true)
    List<AttachmentText>        findTextAttachmentByStudent(int id)               throws StudentNotFoundException;

    @Transactional(readOnly = true)
    AttachmentHeader findAttachmentHeaderById(int id)                             throws AttachmentNotFoundException;

    @Transactional(readOnly = true)
    Integer testValidation(@NotNull Integer arg);
}
