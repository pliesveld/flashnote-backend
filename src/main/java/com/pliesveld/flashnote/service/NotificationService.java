package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Notification;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Instant;
import java.util.List;

@Validated
@Transactional(readOnly = true)
public interface NotificationService {

    @Transactional
    Notification sendSystemMessage(final int student_id, final String message) throws StudentNotFoundException;

    @Transactional
    Notification sendSystemErrorMessage(final int student_id, final String message) throws StudentNotFoundException;

    List<Notification> retrieveStudentMessages(final int student_id) throws StudentNotFoundException;

    List<Notification> retrieveStudentMessagesSince(final int student_id, final Instant last_login) throws StudentNotFoundException;
}
