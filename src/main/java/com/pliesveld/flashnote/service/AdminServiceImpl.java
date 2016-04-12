package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.repository.*;
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
    StudentRepository studentRepository;

    @Autowired
    StudentDetailsRepository studentDetailsRepository;

    @Autowired
    RememberTokenRepository rememberTokenRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    PasswordResetRepository passwordResetRepository;


    @Override
    public void deleteStudent(int id) throws StudentNotFoundException {
        if(!studentRepository.exists(id))
        {
            throw new StudentNotFoundException(id);
        }

        String username = studentRepository.findOne(id).getEmail();

        rememberTokenRepository.deleteTokensForUser(username);

        checkRepositoryAndDelete(registrationRepository,id);
        checkRepositoryAndDelete(passwordResetRepository,id);
        checkRepositoryAndDelete(studentDetailsRepository,id);
        studentRepository.delete(id);
    }

    private <ID extends Serializable, R extends CrudRepository<?,ID>> void checkRepositoryAndDelete(R repository, ID id) {
        if(repository.exists(id))
        {
            repository.delete(id);
        } else {
            LOG.info("Could not delete {} from {}", id, repository.getClass().getSimpleName());
        }
    }

    @Override
    public StudentDetails update(StudentDetails studentDetails) throws StudentNotFoundException {
        return studentDetailsRepository.save(studentDetails);
    }

    @Override
    public List<StudentDetails> findAllStudentDetails() {
        return studentDetailsRepository.findAllAsStream().collect(Collectors.toList());
    }

    @Override
    public Student createStudent(String name, String email, String password) {


        Student student = new Student();
        student.setEmail(email);
        student.setPassword(password);

        StudentDetails studentDetails = new StudentDetails(name);

        student.setStudentDetails(studentDetails);
        studentRepository.save(student);
        studentDetailsRepository.save(studentDetails);
        student.setRole(StudentRole.ROLE_USER);
        return student;
    }
}
