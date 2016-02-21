package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Attachment;
import com.pliesveld.flashnote.domain.AttachmentHeader;
import com.pliesveld.flashnote.exception.AttachmentNotFoundException;
import com.pliesveld.flashnote.exception.AttachmentUploadException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    Attachment          findAttachmentById(int id)        throws AttachmentNotFoundException;

    @Transactional(rollbackFor = StudentNotFoundException.class)
    Attachment delete(int id) throws AttachmentNotFoundException;

    AttachmentHeader    findAttachmentHeaderById(int id)  throws AttachmentNotFoundException;

    void                removeAttachmentById(int id)      throws AttachmentNotFoundException;
    List<Attachment>    findAttachmentByStudent(int id)   throws StudentNotFoundException;

    Attachment storeAttachment(String name, MultipartFile file) throws AttachmentUploadException;
}
