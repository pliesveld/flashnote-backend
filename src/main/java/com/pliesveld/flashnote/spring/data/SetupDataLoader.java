package com.pliesveld.flashnote.spring.data;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.pliesveld.flashnote.domain.StudentRole.*;



@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if(alreadySetup)
            return;

        createStudentIfNotFound("new@example.com",     ROLE_ACCOUNT,    "new");
        createStudentIfNotFound("student@example.com", ROLE_USER,       "basic");
        createStudentIfNotFound("premium@example.com", ROLE_PREMIUM,    "premium");
        createStudentIfNotFound("mod@example.com",     ROLE_MODERATOR,  "moderator");
        createStudentIfNotFound("admin@example.com",   ROLE_ADMIN,      "admin");
    }

    @Transactional
    private Student createStudentIfNotFound(String email, StudentRole role, String name) {
        Student student = studentRepository.findOneByEmail(email);
        if( student == null )
        {
            student = new Student();
            student.setEmail(email);
            student.setRole(role);
            student.setPassword(passwordEncoder.encode("password"));

            StudentDetails studentDetails = new StudentDetails(name);
            studentDetails.setStudent(student);
            studentRepository.save(student);
        }

        return student;
    }

/*
    @Transactional
    private final Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
    */

}
