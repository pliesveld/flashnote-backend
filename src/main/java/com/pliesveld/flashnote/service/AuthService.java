package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthService implements UserDetailsService {
    final static Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional(rollbackFor = UsernameNotFoundException.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Student student = studentRepository.findOneByEmail(username);
        logger.info("Student: " + student);

        if(student == null)
        {
            throw new UsernameNotFoundException(String.format("Student with an email address of %s does not exist",username));
        }

        logger.info(" pass " + student.getPassword() + " email " + student.getEmail());

        List<GrantedAuthority> authorities = new ArrayList<>();

        authorities = AuthorityUtils.createAuthorityList(student.getRole().name());

        UserDetails userDetails = new User(student.getEmail(),student.getPassword(),authorities);
        logger.info("userdetails pass: " + userDetails.getPassword() + " username " + userDetails.getUsername() + " authorities " + authorities);
        return userDetails;
    }
}
