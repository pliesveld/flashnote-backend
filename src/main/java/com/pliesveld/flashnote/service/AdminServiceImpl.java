package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AccountRole;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.repository.PasswordResetRepository;
import com.pliesveld.flashnote.repository.RegistrationRepository;
import com.pliesveld.flashnote.repository.RememberTokenRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Service("adminService")
public class AdminServiceImpl implements AdminService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RememberTokenRepository rememberTokenRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PasswordResetRepository passwordResetRepository;


    @Override
    public void deleteStudent(final int id) throws StudentNotFoundException {
        if (!studentRepository.exists(id)) {
            throw new StudentNotFoundException(id);
        }

        final String username = studentRepository.findOne(id).getEmail();

        rememberTokenRepository.deleteTokensForUser(username);

        checkRepositoryAndDelete(registrationRepository, id);
        checkRepositoryAndDelete(passwordResetRepository, id);
        checkRepositoryAndDelete(studentRepository, id);
        studentRepository.delete(id);
    }

    private <ID extends Serializable, R extends CrudRepository<?, ID>> void checkRepositoryAndDelete(final R repository, final ID id) {
        if (repository.exists(id)) {
            repository.delete(id);
        } else {
            LOG.info("Could not delete {} from {}", id, repository.getClass().getSimpleName());
        }
    }

    @Override
    public Student update(final Student student) throws StudentNotFoundException {
        return studentRepository.save(student);
    }

    @Override
    public List<Student> findAllStudent() {
        return studentRepository.findAllAsStream().collect(Collectors.toList());
    }

    @Override
    public Student createStudent(final String name, final String email, final String password) {


        final Student student = new Student();
        student.setEmail(email);
        student.setPassword(password);
        student.setName(name);
        student.setRole(AccountRole.ROLE_USER);
        studentRepository.save(student);
        return student;
    }
}
