package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.domain.StudentRole;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import com.pliesveld.flashnote.repository.DeckRepository;
import com.pliesveld.flashnote.repository.StudentDetailsRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service(value = "studentService")
public class StudentServiceImpl implements StudentService {
    
    private static final Logger LOG = LogManager.getLogger();

    @Resource
    StudentRepository studentRepository;

    @Resource
    StudentDetailsRepository studentDetailsRepository;

    @Resource
    DeckRepository deckRepository;

    @Override
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
    public Iterable<StudentDetails> findAll() {
        return studentDetailsRepository.findAll();
    }

    @Override
    public Student findByEmail(String email) {
        return studentRepository.findOneByEmail(email);
    }

    @Override
    public StudentDetails delete(int id) throws StudentNotFoundException {
        StudentDetails deletedStudentDetails = studentDetailsRepository.findOne(id);
        if(deletedStudentDetails == null)
            throw new StudentNotFoundException(id);

        studentDetailsRepository.delete(deletedStudentDetails);
        return deletedStudentDetails;
    }

    @Override
    public StudentDetails update(StudentDetails studentDetails) throws StudentNotFoundException {
        StudentDetails updatedStudentDetails = studentDetailsRepository.findOne(studentDetails.getId());

        if(updatedStudentDetails == null)
            throw new StudentNotFoundException(studentDetails.getId());

        updatedStudentDetails.setName(studentDetails.getName());
        return updatedStudentDetails;
    }

    @Override
    public Long count() {
        return studentDetailsRepository.count();
    }
}
