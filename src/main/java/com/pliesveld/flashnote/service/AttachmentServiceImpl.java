package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.domain.dto.AttachmentHeader;
import com.pliesveld.flashnote.exception.AttachmentNotFoundException;
import com.pliesveld.flashnote.exception.AttachmentUploadException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.repository.AttachmentBinaryRepository;
import com.pliesveld.flashnote.repository.AttachmentTextRepository;
import com.pliesveld.flashnote.web.validator.ValidAttachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static com.pliesveld.flashnote.logging.Markers.SERVICE_ATTACHMENT;

@Service(value = "attachmentService")
public class AttachmentServiceImpl implements AttachmentService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    AttachmentBinaryRepository attachmentBinaryRepository;

    @Autowired
    AttachmentTextRepository attachmentTextRepository;

    @Autowired
    StudentService studentService;

    private StudentDetails verifyStudentDetails(int id) throws StudentNotFoundException
    {
        StudentDetails studentDetails = studentService.findStudentDetailsById(id);
        if(studentDetails == null)
            throw new StudentNotFoundException(id);

        return studentDetails;
    }

    private Student verifyStudent(int id) throws StudentNotFoundException
    {
        Student student = studentService.findStudentById(id);
        if(student == null)
            throw new StudentNotFoundException(id);

        return student;
    }

    private AbstractAttachment verifyAttachment(int id) throws AttachmentNotFoundException
    {
        AttachmentBinary attachmentBin = attachmentBinaryRepository.findOne(id);
        if(attachmentBin == null)
        {
            AttachmentText attachmentText = attachmentTextRepository.findOne(id);
            if(attachmentText == null)
                throw new AttachmentNotFoundException(id);

            return attachmentText;
        }
        return attachmentBin;
    }

    private AttachmentBinary verifyAttachmentBinary(int id) throws AttachmentNotFoundException
    {
        AttachmentBinary attachmentBin = attachmentBinaryRepository.findOne(id);
        if(attachmentBin == null)
            throw new AttachmentNotFoundException(id);
        return attachmentBin;
    }

    private AttachmentText verifyAttachmentText(int id) throws AttachmentNotFoundException
    {
        AttachmentText attachmentText = attachmentTextRepository.findOne(id);
        if(attachmentText == null)
            throw new AttachmentNotFoundException(id);

        return attachmentText;
    }


    @Override
    public List<AttachmentBinary> findBinaryAttachmentByStudentEmail(String email) throws StudentNotFoundException {
        return attachmentBinaryRepository.findByCreatedByUser(email);
    }

    @Override
    public List<AttachmentText> findTextAttachmentByStudentEmail(String email) throws StudentNotFoundException {
        return attachmentTextRepository.findByCreatedByUser(email);
    }

    @Override
    public List<AttachmentBinary> findBinaryAttachmentByStudent(int id) throws StudentNotFoundException {
        Student student = verifyStudent(id);
        String email;
        if(!StringUtils.hasText((email = student.getEmail())))
        {
            throw new StudentNotFoundException("No email for student id " + id);
        }
        return findBinaryAttachmentByStudentEmail(email);
    }

    @Override
    public List<AttachmentText> findTextAttachmentByStudent(int id) throws StudentNotFoundException {
        Student student = verifyStudent(id);
        String email;
        if(!StringUtils.hasText((email = student.getEmail())))
        {
            throw new StudentNotFoundException("No email for student id " + id);
        }
        return findTextAttachmentByStudentEmail(email);
    }

    @Override
    public AbstractAttachment findAttachmentById(int id) throws AttachmentNotFoundException {
        AbstractAttachment attachment = verifyAttachment(id);
        return attachment;
    }

    @Override
    public AttachmentBinary findAttachmentBinaryById(int id) throws AttachmentNotFoundException {
        AttachmentBinary attachment = verifyAttachmentBinary(id);
        return attachment;
    }

    @Override
    public AttachmentText findAttachmentTextById(int id) throws AttachmentNotFoundException {
        AttachmentText attachment = verifyAttachmentText(id);
        return attachment;
    }

    @Override
    public void removeAttachmentById(int id) throws AttachmentNotFoundException {
        if(attachmentBinaryRepository.exists(id))
        {
            attachmentBinaryRepository.delete(id);
        } else if(attachmentTextRepository.exists(id)) {
            attachmentTextRepository.delete(id);
        } else {
            throw new AttachmentNotFoundException("Could not find id " + id);
        }
    }

    @Override
    public List<AbstractAttachment> findAttachmentByStudent(int id) throws StudentNotFoundException {

        StudentDetails studentDetails = verifyStudentDetails(id);
        String email = studentDetails.getStudent().getEmail();
        List<AbstractAttachment> attachmentList = new ArrayList<>();

        attachmentBinaryRepository.findByCreatedByUser(email).forEach(attachmentList::add);
        attachmentTextRepository.findByCreatedByUser(email).forEach(attachmentList::add);

        return attachmentList;
    }

    @Override
    public AttachmentText storeAttachment(@ValidAttachment AttachmentText attachment) throws AttachmentUploadException {
        LOG.debug(SERVICE_ATTACHMENT,"Storing attachment {} {} {}", attachment.getAttachmentType(),
                attachment.getFileName(),attachment.getMimeType());

        return attachmentTextRepository.save(attachment);
    }


    @Override
    public AttachmentBinary storeAttachment(@ValidAttachment AttachmentBinary attachment) throws AttachmentUploadException {

        LOG.debug(SERVICE_ATTACHMENT,"Storing attachment {} {} {}", attachment.getAttachmentType(),
                attachment.getFileName(),attachment.getMimeType());

        return attachmentBinaryRepository.save(attachment);
    }

    @Override
    public AttachmentBinary delete(int id) throws AttachmentNotFoundException {
        AttachmentBinary attachment = attachmentBinaryRepository.findOne(id);
        if(attachment == null)
            throw new AttachmentNotFoundException(id);

        attachmentBinaryRepository.delete(attachment);
        return attachment;
    }

    @Override
    public AttachmentHeader findAttachmentHeaderById(int id) throws AttachmentNotFoundException {
        return attachmentBinaryRepository.findAttachmentHeaderById(id);
    }

    @Override
    public Integer testValidation(@NotNull Integer arg) {
        LOG.debug("arg = " + arg);
        return arg;
    }
}
