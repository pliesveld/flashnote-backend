package com.pliesveld.flashnote.security;

import com.pliesveld.flashnote.domain.Student;
import org.springframework.context.ApplicationEvent;

/**
 * Created by patrick on 5/20/16.
 */
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private final Student student;
    public OnRegistrationCompleteEvent(final Student student) {
        super(student);
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}