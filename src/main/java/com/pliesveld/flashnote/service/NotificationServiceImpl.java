package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Notification;
import com.pliesveld.flashnote.domain.NotificationType;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.repository.NotificationRepository;
import com.pliesveld.flashnote.repository.StudentDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service("notificationService")
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private StudentDetailsRepository studentDetailsRepository;

    private StudentDetails verifyStudent(final int id) throws StudentNotFoundException {
        StudentDetails studentDetails = studentDetailsRepository.findOne(id);
        if(studentDetails == null) {
            throw new StudentNotFoundException(id);
        }
        return studentDetails;
    }

    @Override
    public Notification sendSystemMessage(final int student_id, final String message) {
        StudentDetails studentDetails = verifyStudent(student_id);
        Notification notification = new Notification(NotificationType.SYSTEM_INFO, studentDetails,message);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification sendSystemErrorMessage(final int student_id, final String message) {
        StudentDetails studentDetails = verifyStudent(student_id);
        Notification notification = new Notification(studentDetails,message);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> retrieveStudentMessages(final int student_id) {
        StudentDetails studentDetails = verifyStudent(student_id);
        return notificationRepository.findByRecipientOrderByCreatedOnAsc(studentDetails).collect(Collectors.toList());
    }


    @Override
    public List<Notification> retrieveStudentMessagesSince(int student_id, final Instant last_login) throws StudentNotFoundException {
        StudentDetails studentDetails = verifyStudent(student_id);
        return notificationRepository.findByRecipientAndCreatedOnLessThanOrderByCreatedOnAsc(studentDetails, last_login).collect(Collectors.toList());
    }
}
