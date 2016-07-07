package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AbstractAttachment;
import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.domain.AttachmentText;
import com.pliesveld.flashnote.exception.AttachmentNotFoundException;
import com.pliesveld.flashnote.exception.AttachmentUploadException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.model.json.response.AttachmentHeader;
import com.pliesveld.flashnote.web.validator.ValidAttachment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Transactional(readOnly = true)
public interface AttachmentService {
    Long countAttachments();

    AbstractAttachment findAttachmentById(final int id) throws AttachmentNotFoundException;

    AttachmentBinary findAttachmentBinaryById(final int id) throws AttachmentNotFoundException;

    AttachmentText findAttachmentTextById(final int id) throws AttachmentNotFoundException;

    @Transactional
    @NotNull
    AttachmentBinary storeAttachment(@ValidAttachment final AttachmentBinary attachment) throws AttachmentUploadException;

    @Transactional
    @NotNull
    AttachmentText storeAttachment(@ValidAttachment final AttachmentText attachment) throws AttachmentUploadException;

    @Transactional
    void removeAttachmentById(final int id) throws AttachmentNotFoundException;

    @NotNull
    List<AbstractAttachment> findAttachmentByStudent(final int id) throws StudentNotFoundException;

    @NotNull
    List<AttachmentBinary> findBinaryAttachmentByStudentEmail(final String email) throws StudentNotFoundException;

    @NotNull
    List<AttachmentText> findTextAttachmentByStudentEmail(final String email) throws StudentNotFoundException;

    @NotNull
    List<AttachmentBinary> findBinaryAttachmentByStudent(final int id) throws StudentNotFoundException;

    @NotNull
    List<AttachmentText> findTextAttachmentByStudent(final int id) throws StudentNotFoundException;

    @NotNull
    AttachmentHeader findAttachmentHeaderById(final int id) throws AttachmentNotFoundException;

    List<AbstractAttachment> findAllAttachments();
}
