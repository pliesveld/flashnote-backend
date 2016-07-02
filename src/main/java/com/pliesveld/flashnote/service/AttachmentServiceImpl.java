package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AbstractAttachment;
import com.pliesveld.flashnote.domain.AttachmentBinary;
import com.pliesveld.flashnote.domain.AttachmentText;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.AttachmentNotFoundException;
import com.pliesveld.flashnote.exception.AttachmentUploadException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.model.json.response.AttachmentHeader;
import com.pliesveld.flashnote.repository.AttachmentBinaryRepository;
import com.pliesveld.flashnote.repository.AttachmentRepository;
import com.pliesveld.flashnote.repository.AttachmentTextRepository;
import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.flashnote.web.validator.ValidAttachment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.pliesveld.flashnote.logging.Markers.SERVICE_ATTACHMENT;

@Service(value = "attachmentService")
public class AttachmentServiceImpl implements AttachmentService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AttachmentBinaryRepository attachmentBinaryRepository;

    @Autowired
    private AttachmentTextRepository attachmentTextRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private StudentService studentService;

    private Student verifyStudent(final int id) throws StudentNotFoundException
    {
        final Student student = studentService.findStudentById(id);
        if (student == null)
            throw new StudentNotFoundException(id);

        return student;
    }

    private AbstractAttachment verifyAttachment(final int id) throws AttachmentNotFoundException
    {
        final AbstractAttachment attachment = attachmentRepository.findOneById(id);
        if ( attachment == null )
            throw new AttachmentNotFoundException(id);

        return attachment;
    }

    private AttachmentBinary verifyAttachmentBinary(final int id) throws AttachmentNotFoundException
    {
        final AttachmentBinary attachmentBin = attachmentBinaryRepository.findOne(id);

        if (attachmentBin == null)
            throw new AttachmentNotFoundException(id);
        return attachmentBin;
    }

    private AttachmentText verifyAttachmentText(final int id) throws AttachmentNotFoundException
    {
        final AttachmentText attachmentText = attachmentTextRepository.findOne(id);

        if (attachmentText == null)
            throw new AttachmentNotFoundException(id);
        return attachmentText;
    }


    @Override
    public Long countAttachments() {
        return attachmentBinaryRepository.count() + attachmentTextRepository.count();
    }

    @Override
    public List<AttachmentBinary> findBinaryAttachmentByStudentEmail(final String email) throws StudentNotFoundException {
        return attachmentBinaryRepository.findAllByAuthor(email).collect(Collectors.toList());
    }

    @Override
    public List<AttachmentText> findTextAttachmentByStudentEmail(final String email) throws StudentNotFoundException {
        return attachmentTextRepository.findAllByAuthor(email).collect(Collectors.toList());
    }

    @Override
    public List<AttachmentBinary> findBinaryAttachmentByStudent(final int id) throws StudentNotFoundException {
        final Student student = verifyStudent(id);
        final String email;
        if (!StringUtils.hasText((email = student.getEmail())))
        {
            throw new StudentNotFoundException("No email for student id " + id);
        }
        return findBinaryAttachmentByStudentEmail(email);
    }

    @Override
    public List<AttachmentText> findTextAttachmentByStudent(final int id) throws StudentNotFoundException {
        final Student student = verifyStudent(id);
        final String email;
        if (!StringUtils.hasText((email = student.getEmail())))
        {
            throw new StudentNotFoundException("No email for student id " + id);
        }
        return findTextAttachmentByStudentEmail(email);
    }

    @Override
    public AbstractAttachment findAttachmentById(final int id) throws AttachmentNotFoundException {
        final AbstractAttachment attachment = verifyAttachment(id);
        return attachment;
    }

    @Override
    public AttachmentBinary findAttachmentBinaryById(final int id) throws AttachmentNotFoundException {
        final AttachmentBinary attachment = verifyAttachmentBinary(id);
        return attachment;
    }

    @Override
    public AttachmentText findAttachmentTextById(final int id) throws AttachmentNotFoundException {
        final AttachmentText attachment = verifyAttachmentText(id);
        return attachment;
    }

    @Override
    public void removeAttachmentById(final int id) throws AttachmentNotFoundException {
        if (attachmentRepository.exists(id))
        {
            questionRepository.findByAttachmentId(id).forEach((stmt) -> stmt.setAttachment(null));

            attachmentRepository.delete(id);
        }
//
//        if (attachmentBinaryRepository.exists(id))
//        {
//
//
//        } else if (attachmentTextRepository.exists(id)) {
//            questionRepository.findByAttachmentId(id).forEach((stmt) -> stmt.setAttachment(null));
//            attachmentRepository.delete(id);
//        } else {
//            throw new AttachmentNotFoundException("Could not find id " + id);
//        }
    }

    @Override
    public List<AbstractAttachment> findAttachmentByStudent(final int id) throws StudentNotFoundException {

        final Student student = verifyStudent(id);
        final String email = student.getEmail();

        return attachmentRepository.findAllByAuthor(email).collect(Collectors.toList());
    }

    @Override
    public AttachmentBinary storeAttachment(@ValidAttachment final AttachmentBinary attachment) throws AttachmentUploadException {

        LOG.debug(SERVICE_ATTACHMENT,"Storing attachment {} {} {}", attachment.getAttachmentType(),
                attachment.getFileName(),attachment.getMimeContentType());

        return attachmentBinaryRepository.save(attachment);
    }

    @Override
    public AttachmentText storeAttachment(@ValidAttachment final AttachmentText attachment) throws AttachmentUploadException {
        LOG.debug(SERVICE_ATTACHMENT,"Storing attachment {} {} {}", attachment.getAttachmentType(),
                attachment.getFileName(),attachment.getMimeContentType());

        return attachmentTextRepository.save(attachment);
    }

    @Override
    public AttachmentHeader findAttachmentHeaderById(final int id) throws AttachmentNotFoundException {
        return attachmentRepository.findAttachmentHeaderById(id);
    }

}
