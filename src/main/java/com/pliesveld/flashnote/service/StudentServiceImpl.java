package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AbstractStatement;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.repository.DeckRepository;
import com.pliesveld.flashnote.repository.StatementRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service(value = "studentService")
public class StudentServiceImpl implements StudentService {
    
    private static final Logger LOG = LogManager.getLogger();

    @Resource
    private StudentRepository studentRepository;

    @Resource
    private DeckRepository deckRepository;

    @Resource
    private StatementRepository statementRepository;

    @Override
    public List<Deck> findDecksByOwner(final int id) {
        return deckRepository.findByOwner(id);
    }

    @Override
    public Student findStudentById(final int id) {
        return studentRepository.findOne(id);
    }

    @Override
    public Page<Student> findAll(final Pageable page) {
        return studentRepository.findAll(page);
    }

    @Override
    public Student findByEmail(final String email) {
        return studentRepository.findOneByEmail(email);
    }

    @Override
    public Student findByName(final String name) {
        return studentRepository.findByName(name);
    }

    @Override
    public Student delete(final int student_id) throws StudentNotFoundException {

        // delete RegistrationToken
        // delete PasswordResetToken
        // delete Deck

        Student student = studentRepository.findOne(student_id);
        if(student == null)
            throw new StudentNotFoundException(student_id);

        studentRepository.delete(student);
        return student;
    }

    @Override
    public Long count() {
        return studentRepository.count();
    }

    @Override
    public List<AbstractStatement> findStatementsBy(final Student student) throws StudentNotFoundException {

        if(student == null)
        {
            throw new StudentNotFoundException(student.getId());
        }

        final String email = student.getEmail();
        final List<AbstractStatement> list = statementRepository.findAllByAuthor(email).collect(Collectors.toList());
        return list;
    }
}
