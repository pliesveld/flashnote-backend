package com.pliesveld.flashnote.security;

import com.pliesveld.flashnote.domain.Student;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Transactional(rollbackFor = UsernameNotFoundException.class)
final public class StudentPrincipal extends Student implements UserDetails {

    private List<GrantedAuthority> authorities;

    public StudentPrincipal(Student student, List<GrantedAuthority> authorities) {
        super(student);
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return true;
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    private static final long serialVersionUID = 5639683223516504866L;
}
