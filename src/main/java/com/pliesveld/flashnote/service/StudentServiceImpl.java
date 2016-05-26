package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AbstractStatement;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.repository.DeckRepository;
import com.pliesveld.flashnote.repository.StatementRepository;
import com.pliesveld.flashnote.repository.StudentDetailsRepository;
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
    private StudentDetailsRepository studentDetailsRepository;

    @Resource
    private DeckRepository deckRepository;

    @Resource
    private StatementRepository statementRepository;

    @Override
    public List<Deck> findDecksByOwner(final int id) {
        return deckRepository.findByOwner(id);
    }

    @Override
    public StudentDetails findStudentDetailsById(final int id) {
        return studentDetailsRepository.findOne(id);
    }

    @Override
    public Student findStudentById(final int id) {
        return studentRepository.findOne(id);
    }

    @Override
    public Page<StudentDetails> findAll(final Pageable page) {
        return studentDetailsRepository.findAll(page);
    }

    @Override
    public Student findByEmail(final String email) {
        return studentRepository.findOneByEmail(email);
    }

    @Override
    public StudentDetails findByName(final String name) {
        return studentDetailsRepository.findByName(name);
    }

    @Override
    public StudentDetails delete(final int id) throws StudentNotFoundException {

        // delete RegistrationToken
        // delete PasswordResetToken
        // delete Deck

        StudentDetails deletedStudentDetails = studentDetailsRepository.findOne(id);
        if(deletedStudentDetails == null)
            throw new StudentNotFoundException(id);

        studentDetailsRepository.delete(deletedStudentDetails);
        return deletedStudentDetails;
    }

    @Override
    public Long count() {
        return studentDetailsRepository.count();
    }

    @Override
    public List<AbstractStatement> findStatementsBy(final StudentDetails studentDetails) throws StudentNotFoundException {
        Student student = studentDetails.getStudent();

        if(student == null)
        {
            throw new StudentNotFoundException(studentDetails.getId());
        }

        final String email = studentDetails.getStudent().getEmail();
        final List<AbstractStatement> list = statementRepository.findAllByAuthor(email).collect(Collectors.toList());
        return list;
    }
}
