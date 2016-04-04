package com.pliesveld.flashnote.security;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.pliesveld.flashnote.logging.Markers.SECURITY_AUTH;

@Component
@Transactional
public class AuthService implements UserDetailsService {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Student student = studentRepository.findOneByEmail(username);

        if(student == null)
        {
            throw new UsernameNotFoundException(String.format("Student with an email address of %s does not exist",username));
        }

        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(student.getRole().toString());

        if(LOG.isDebugEnabled())
        {
            StringBuilder sb = new StringBuilder(128);
            sb.append("Loading UserDetails for ").append(username).append(".  Granted Authorities: ");
            authorities.forEach((a) -> sb.append(a).append(" "));
            LOG.debug(SECURITY_AUTH, sb.toString());
        }

        return new StudentPrincipal(student,authorities);
    }
}


