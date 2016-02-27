package com.pliesveld.flashnote.security;

import com.pliesveld.flashnote.domain.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Transactional(rollbackFor = UsernameNotFoundException.class)
final public class StudentPrincipal extends User implements UserDetails {


    public StudentPrincipal(Student student, Collection<GrantedAuthority> authorities) {
        super(student.getEmail(),student.getPassword(),authorities);
    }

    private static final long serialVersionUID = 5639683223516504866L;
}
