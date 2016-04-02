package com.pliesveld.flashnote.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.pliesveld.flashnote.domain.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@JsonInclude(JsonInclude.Include.ALWAYS)
final public class StudentPrincipal extends User implements UserDetails {
    private static final long serialVersionUID = 5639683223516504866L;

    final String handle;

    public String getHandle() {
        return handle;
    }

    public StudentPrincipal(Student student, Collection<GrantedAuthority> authorities) {
        super(student.getEmail(),student.getPassword(),authorities);
        handle = student.getStudentDetails().getName();
    }


}
