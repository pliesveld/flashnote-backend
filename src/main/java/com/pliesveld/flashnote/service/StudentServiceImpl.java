package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentType;
import com.pliesveld.flashnote.exception.ResourceNotFoundException;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
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

    @Override
    @Transactional(rollbackFor = StudentCreateException.class)
    public Student create(String name, String email, String password) throws StudentCreateException {
        
        Student other = studentRepository.findOneByEmail(email);

        if(other != null)
            throw new StudentCreateException(email);

        
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(password);
        student.setRole(StudentType.USER);

        try {
            student = studentRepository.save(student);
        } catch(DataAccessException cdae) {
            throw new StudentCreateException("Could not create student account");
        }

        return student;
    }

    @Override
    @Transactional(rollbackFor = ResourceNotFoundException.class)
    public List<Deck> findDecksByOwner(int id) {
        Student student = findById(id);
        List<Deck> decks = new ArrayList<>();
        student.getDecks().forEach(decks::add);
        return decks;
    }

    @Override
    @Transactional(readOnly = true)
    public Student findById(int id) {
        return studentRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Student findByEmail(String email) {
        return studentRepository.findOneByEmail(email);
    }

    @Override
    @Transactional(rollbackFor = StudentNotFoundException.class)
    public Student delete(int id) throws StudentNotFoundException {
        Student deletedStudent = studentRepository.findOne(id);
        if(deletedStudent == null)
            throw new StudentNotFoundException(id);

        studentRepository.delete(deletedStudent);
        return deletedStudent;
    }

    @Override
    @Transactional(rollbackFor = StudentNotFoundException.class)
    public Student update(Student student) throws StudentNotFoundException {
        Student updatedStudent = studentRepository.findOne(student.getId());

        if(updatedStudent == null)
            throw new StudentNotFoundException(student.getId());

        updatedStudent.setName(student.getName());
        updatedStudent.setEmail(student.getEmail());
        updatedStudent.setPassword(student.getPassword());
        return updatedStudent;
    }

    @Override
    @Transactional(readOnly = true)
    public Long count() {
        return studentRepository.count();
    }
}
