package com.pliesveld.flashnote.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pliesveld.flashnote.domain.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;

@JsonIgnoreProperties( value = {"password", "accountNonExpired", "accountNonLocked", "enabled" })
final public class StudentPrincipal extends User implements UserDetails {
    private static final long serialVersionUID = 5639683223516504866L;

    final String handle;
    private Instant lastPasswordResetDate;

    public StudentPrincipal(Student student, Collection<GrantedAuthority> authorities) {
        super(student.getEmail(),student.getPassword(),authorities);
        handle = student.getStudentDetails().getName();
        lastPasswordResetDate = student.getLastPasswordResetDate();
    }

    public String getHandle() {
        return handle;
    }

    public Instant getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }
}
