package com.pliesveld.flashnote.security;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Transactional(rollbackFor = UsernameNotFoundException.class)
public class AuthService extends AbstractUserDetailsAuthenticationProvider implements UserDetailsService {
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


        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(student.getRole().toString());
        authorities.forEach((a) -> LOG.info(a.getAuthority()));

        return new StudentPrincipal(student,authorities);

//        UserDetails userDetails = new User(student.getEmail(),student.getPassword(),authorities);
//        return userDetails;
//        LOG.info("userdetails pass: " + userDetails.getPassword() + " username " + userDetails.getUsername() + " authorities " + authorities);
//        return userDetails;
    }

    private Collection<GrantedAuthority> buildUserAuthority(Set<StudentRole> userRoles) {
        Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
        for(StudentRole studentRole: userRoles) {
            setAuths.add(new SimpleGrantedAuthority(studentRole.toString()));
        }
        return setAuths;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Authentication authenticated = new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return authentication.getAuthorities();
            }

            @Override
            public Object getCredentials() {
                return authentication.getCredentials();
            }

            @Override
            public Object getDetails() {
                return authentication.getDetails();
            }

            @Override
            public Object getPrincipal() {
                return authentication.getPrincipal();
            }

            @Override
            public boolean isAuthenticated() {
                return true;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
                setAuthenticated(isAuthenticated);
            }

            @Override
            public String getName() {
                return authentication.getName();
            }
        };

        return authenticated;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        LOG.info("additionalChecks {}",userDetails.getUsername());
        authentication.setAuthenticated(true);
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        LOG.info("retrieveUser {}",username);
        //authentication.setAuthenticated(true);
        Student student = studentRepository.findOneByEmail(username);

        return new StudentPrincipal(student,authentication.getAuthorities());
    }
}


