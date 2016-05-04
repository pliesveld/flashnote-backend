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
    StudentRepository studentRepository;

    @Resource
    StudentDetailsRepository studentDetailsRepository;

    @Resource
    DeckRepository deckRepository;

    @Resource
    StatementRepository statementRepository;



    @Override
    public List<Deck> findDecksByOwner(int id) {
        return deckRepository.findByAuthor_Id(id);
    }

    @Override
    public StudentDetails findStudentDetailsById(int id) {
        return studentDetailsRepository.findOne(id);
    }

    @Override
    public Student findStudentById(int id) {
        return studentRepository.findOne(id);
    }

    @Override
    public Page<StudentDetails> findAll(Pageable page) {
        return studentDetailsRepository.findAll(page);
    }

    @Override
    public Student findByEmail(String email) {
        return studentRepository.findOneByEmail(email);
    }

    @Override
    public StudentDetails findByName(String name) {
        return studentDetailsRepository.findByName(name);
    }

    @Override
    public StudentDetails delete(int id) throws StudentNotFoundException {

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
    public List<AbstractStatement> findStatementsBy(StudentDetails studentDetails) throws StudentNotFoundException {
        Student student = studentDetails.getStudent();

        if(student == null)
        {
            throw new StudentNotFoundException(studentDetails.getId());
        }

        String email = studentDetails.getStudent().getEmail();
        List<AbstractStatement> list = statementRepository.findAllByAuthor(email).collect(Collectors.toList());
        return list;
    }
}
