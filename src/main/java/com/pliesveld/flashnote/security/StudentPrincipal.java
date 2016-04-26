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

    private int id;
    private String handle;
    private Instant lastPasswordResetDate;

    public StudentPrincipal(Student student, Collection<GrantedAuthority> authorities) {
        super(student.getEmail(),student.getPassword(),authorities);
        handle = student.getStudentDetails().getName();
        lastPasswordResetDate = student.getLastPasswordResetDate();
        id = student.getId();
    }

    public String getHandle() {
        return handle;
    }

    public Instant getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public int getId() { return id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StudentPrincipal)) return false;
        if (!super.equals(o)) return false;

        StudentPrincipal that = (StudentPrincipal) o;

        if (!handle.equals(that.handle)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + handle.hashCode();
        return result;
    }
}
