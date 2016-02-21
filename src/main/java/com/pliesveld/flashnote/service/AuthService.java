package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
@Transactional(rollbackFor = UsernameNotFoundException.class)
public class AuthService implements UserDetailsService {
    final static Logger LOG = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional(rollbackFor = UsernameNotFoundException.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("Looking up email address: " + username);
        Student student = studentRepository.findOneByEmail(username);
        LOG.info("Email: " + username + " -> " + student);

        if(student == null)
        {
            throw new UsernameNotFoundException(String.format("Student with an email address of %s does not exist",username));
        }

        LOG.info(" pass " + student.getPassword() + " email " + student.getEmail());

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(student.getRole().name());
        return new CustomUserDetails(student,authorities);

//        UserDetails userDetails = new User(student.getEmail(),student.getPassword(),authorities);
//        LOG.info("userdetails pass: " + userDetails.getPassword() + " username " + userDetails.getUsername() + " authorities " + authorities);
//        return userDetails;
    }

    @Transactional(rollbackFor = UsernameNotFoundException.class)
    private final static class CustomUserDetails extends Student implements UserDetails {

        private List<GrantedAuthority> authorities;
/*
        private CustomUserDetails(Student user) {
            super(user);
        }
*/
        public CustomUserDetails(Student student, List<GrantedAuthority> authorities) {
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
}
