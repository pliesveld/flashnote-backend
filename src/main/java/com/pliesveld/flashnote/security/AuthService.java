package com.pliesveld.flashnote.security;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.logging.Markers;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import static com.pliesveld.flashnote.logging.Markers.SECURITY_AUTH;

@Component
@Transactional(rollbackFor = UsernameNotFoundException.class)
public class AuthService extends AbstractUserDetailsAuthenticationProvider implements UserDetailsService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional(rollbackFor = UsernameNotFoundException.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info(SECURITY_AUTH, "Looking up email address: " + username);
        Student student = studentRepository.findOneByEmail(username);


        if(student == null)
        {
            throw new UsernameNotFoundException(String.format("Student with an email address of %s does not exist",username));
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(student.getRole().toString());

        LOG.info(SECURITY_AUTH, "Email: " + username + " -> " + student);
        authorities.forEach((a) -> LOG.info(SECURITY_AUTH,a.getAuthority()));

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
        LOG.info(Markers.SECURITY_AUTH,"additionalChecks {}",userDetails.getUsername());
        authentication.setAuthenticated(true);
    }

    @Override
    protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        LOG.info(Markers.SECURITY_AUTH,"retrieveUser {}",username);
        //authentication.setAuthenticated(true);
        Student student = studentRepository.findOneByEmail(username);

        return new StudentPrincipal(student,authentication.getAuthorities());
    }
}


