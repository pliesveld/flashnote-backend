package com.pliesveld.flashnote.security;

import com.pliesveld.flashnote.domain.Student;
import org.springframework.context.ApplicationEvent;


public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private static final long serialVersionUID = 7277444231437610716L;
    private final Student student;

    public OnRegistrationCompleteEvent(final Student student) {
        super(student);
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}
