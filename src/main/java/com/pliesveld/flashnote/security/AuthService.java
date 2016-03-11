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
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import static com.pliesveld.flashnote.logging.Markers.SECURITY_AUTH;

@Component
@Transactional(rollbackFor = UsernameNotFoundException.class)
public class AuthService /*extends AbstractUserDetailsAuthenticationProvider*/ implements UserDetailsService {

    static {
        try {
            RANDOM = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError("Could not initialize secure random source in AuthService");
        }
    }

    private static final Logger LOG = LogManager.getLogger();
    private static final int HASING_ROUNDS = 10;
    private static final SecureRandom RANDOM;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional(rollbackFor = UsernameNotFoundException.class)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        LOG.info("Looking up email address: " + username);
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

    @Transactional
    public void saveUser(Student principal, String newPassword)
    {
        if(StringUtils.hasLength(newPassword))
        {
            String salt = BCrypt.gensalt(HASING_ROUNDS,RANDOM);
            principal.setPassword(BCrypt.hashpw(newPassword,salt));
        }

        this.studentRepository.save(principal);
    }

}


