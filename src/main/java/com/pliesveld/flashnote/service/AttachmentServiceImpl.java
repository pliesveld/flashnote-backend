package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Attachment;
import com.pliesveld.flashnote.domain.AttachmentHeader;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.AttachmentNotFoundException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.repository.AttachmentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Service(value = "attachmentService")
public class AttachmentServiceImpl implements AttachmentService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    AttachmentRepository attachmentRepository;

    @Autowired
    StudentService studentService;


    private StudentDetails verifyStudent(int id) throws StudentNotFoundException
    {
        StudentDetails studentDetails = studentService.findById(id);
        if(studentDetails == null)
            throw new StudentNotFoundException(id);

        return studentDetails;
    }

    private Attachment verifyAttachment(int id) throws AttachmentNotFoundException
    {
        Attachment attachment = attachmentRepository.findOne(id);
        if(attachment == null)
            throw new AttachmentNotFoundException(id);
        return attachment;
    }


    @Override
    @Transactional(readOnly = true)
    public Attachment findAttachmentById(int id) throws AttachmentNotFoundException {
        Attachment attachment = verifyAttachment(id);
        return attachment;
    }

    @Override
    public void removeAttachmentById(int id) throws AttachmentNotFoundException {
        if(!attachmentRepository.exists(id))
            throw new AttachmentNotFoundException("Could not find id " + id);

        attachmentRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Attachment> findAttachmentByStudent(int id) throws StudentNotFoundException {

        StudentDetails studentDetails = verifyStudent(id);
        return new ArrayList<Attachment>();
    }

    @Override
    @Transactional
    public Attachment storeAttachment(Attachment attachment) {

        LOG.debug("Storing attachment {} {} {}", attachment.getAttachmentType(),
                attachment.getFileName(),attachment.getMimeType());

        return attachmentRepository.save(attachment);
    }

    @Override
    @Transactional(rollbackFor = StudentNotFoundException.class)
    public Attachment delete(int id) throws AttachmentNotFoundException {
        Attachment attachment = attachmentRepository.findOne(id);
        if(attachment == null)
            throw new AttachmentNotFoundException(id);

        attachmentRepository.delete(attachment);
        return attachment;
    }

    @Override
    @Transactional(rollbackFor = AttachmentNotFoundException.class)
    public AttachmentHeader findAttachmentHeaderById(int id) throws AttachmentNotFoundException {
        return attachmentRepository.findAttachmentHeaderById(id);
    }

    @Override
    public Integer testValidation(@NotNull Integer arg) {
        LOG.debug("arg = " + arg);
        return arg;
    }
}
