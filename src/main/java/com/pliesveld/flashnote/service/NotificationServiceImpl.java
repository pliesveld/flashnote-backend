package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Notification;
import com.pliesveld.flashnote.domain.NotificationType;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.repository.NotificationRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
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
    private StudentRepository studentRepository;

    private Student verifyStudent(final int id) throws StudentNotFoundException {
        Student student = studentRepository.findOne(id);
        if (student == null) {
            throw new StudentNotFoundException(id);
        }
        return student;
    }

    @Override
    public Notification sendSystemMessage(final int student_id, final String message) {
        Student student = verifyStudent(student_id);
        Notification notification = new Notification(NotificationType.SYSTEM_INFO, student,message);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification sendSystemErrorMessage(final int student_id, final String message) {
        Student student = verifyStudent(student_id);
        Notification notification = new Notification(student,message);
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> retrieveStudentMessages(final int student_id) {
        Student student = verifyStudent(student_id);
        return notificationRepository.findByRecipientOrderByCreatedOnAsc(student).collect(Collectors.toList());
    }

    @Override
    public List<Notification> retrieveStudentMessagesSince(int student_id, final Instant last_login) throws StudentNotFoundException {
        Student student = verifyStudent(student_id);
        return notificationRepository.findByRecipientAndCreatedOnLessThanOrderByCreatedOnAsc(student, last_login).collect(Collectors.toList());
    }
}
