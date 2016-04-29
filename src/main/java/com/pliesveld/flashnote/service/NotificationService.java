package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Notification;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Transactional(readOnly = true)
public interface NotificationService {

    @Transactional
    Notification sendSystemMessage(int student_id, String message)                         throws StudentNotFoundException;

    @Transactional
    Notification sendSystemErrorMessage(int student_id, String message)                    throws StudentNotFoundException;

    List<Notification> retrieveStudentMessages(int student_id)                             throws StudentNotFoundException;

    List<Notification> retrieveStudentMessagesSince(int student_id, Instant last_login)    throws StudentNotFoundException;
}
