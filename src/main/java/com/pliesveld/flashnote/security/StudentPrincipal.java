package com.pliesveld.flashnote.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pliesveld.flashnote.domain.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;

@JsonIgnoreProperties(value = {"password", "accountNonExpired", "accountNonLocked", "enabled"})
final public class StudentPrincipal extends User implements UserDetails {
    private static final long serialVersionUID = 5639683223516504866L;

    private int id;
    private String handle;
    private Instant lastPasswordResetDate;

    public StudentPrincipal(final Student student, final Collection<GrantedAuthority> authorities) {
        super(student.getEmail(), student.getPassword(), authorities);
        lastPasswordResetDate = student.getLastPasswordResetDate();
        id = student.getId();
        handle = student.getName();
    }

    public Instant getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public int getId() {
        return id;
    }

    public String getHandle() {
        return handle;
    }
}
