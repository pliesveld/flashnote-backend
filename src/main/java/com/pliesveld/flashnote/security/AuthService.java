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

@Component
@Transactional(readOnly = true)
public class AuthService implements UserDetailsService {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        final Student student = studentRepository.findOneByEmail(username);

        if (student == null)
        {
            throw new UsernameNotFoundException(String.format("Student with an email address of %s does not exist", username));
        }

        student.getEmail();
        student.getPassword();
        final List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(student.getRole().toString());

//        if (LOG.isDebugEnabled(SECURITY_AUTH))
//        {
//            StringBuilder sb = new StringBuilder(128);
//            sb.append("Loading UserDetails for ").append(username).append(".  Granted Authorities: ");
//            authorities.forEach((a) -> sb.append(a).append(" "));
//            LOG.debug(SECURITY_AUTH, sb.toString());
//        }

        final StudentPrincipal principal = new StudentPrincipal(student,authorities);
        return principal;
    }
}


