package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.exception.ResourceNotFoundException;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.repository.StudentDetailsRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service(value = "studentService")
public class StudentServiceImpl implements StudentService {
    
    private static final Logger LOG = LogManager.getLogger();

    @Resource
    StudentRepository studentRepository;

    @Resource
    StudentDetailsRepository studentDetailsRepository;

    @Override
    @Transactional(rollbackFor = StudentCreateException.class)
    public Student create(String name, String email, String password) throws StudentCreateException {
        
        Student other = studentRepository.findOneByEmail(email);

        if(other != null)
            throw new StudentCreateException(email);
        
        StudentDetails studentDetails = new StudentDetails();
        studentDetails.setName(name);

        Student student = new Student();
        student.setEmail(email);
        student.setPassword(password);
        student.setRole(StudentRole.ROLE_USER);

        student.setStudentDetails(studentDetails);
        studentDetails.setStudent(student);

        try {
            student = studentRepository.save(student);
            studentDetails = studentDetailsRepository.save(studentDetails);
        } catch(DataAccessException cdae) {
            throw new StudentCreateException("Could not create studentDetails account");
        }

        return student;
    }

    @Override
    @Transactional(rollbackFor = ResourceNotFoundException.class)
    public List<Deck> findDecksByOwner(int id) {
        StudentDetails studentDetails = findStudentDetailsById(id);
        List<Deck> decks = new ArrayList<>();
        studentDetails.getDecks().forEach(decks::add);
        return decks;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDetails findStudentDetailsById(int id) {
        return studentDetailsRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Student findStudentById(int id) {
        return studentRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<StudentDetails> findAll() {
        return studentDetailsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Student findByEmail(String email) {
        return studentRepository.findOneByEmail(email);
    }

    @Override
    @Transactional(rollbackFor = StudentNotFoundException.class)
    public StudentDetails delete(int id) throws StudentNotFoundException {
        StudentDetails deletedStudentDetails = studentDetailsRepository.findOne(id);
        if(deletedStudentDetails == null)
            throw new StudentNotFoundException(id);

        studentDetailsRepository.delete(deletedStudentDetails);
        return deletedStudentDetails;
    }

    @Override
    @Transactional(rollbackFor = StudentNotFoundException.class)
    public StudentDetails update(StudentDetails studentDetails) throws StudentNotFoundException {
        StudentDetails updatedStudentDetails = studentDetailsRepository.findOne(studentDetails.getId());

        if(updatedStudentDetails == null)
            throw new StudentNotFoundException(studentDetails.getId());

        updatedStudentDetails.setName(studentDetails.getName());
        return updatedStudentDetails;
    }

    @Override
    @Transactional(readOnly = true)
    public Long count() {
        return studentDetailsRepository.count();
    }
}
