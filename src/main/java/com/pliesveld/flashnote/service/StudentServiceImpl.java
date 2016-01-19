package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentNotFound;
import com.pliesveld.flashnote.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by happs on 1/15/16.
 */

@Service(value = "studentService")
public class StudentServiceImpl implements StudentService {

    @Resource
    StudentRepository studentRepository;

    @Override
    @Transactional
    public CREATE_RESULT create(String name, String email, String password) {
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(password);

        Student other = studentRepository.findOneByEmail(email);
        if(other != null)
            return CREATE_RESULT.EMAIL_TAKEN;


        studentRepository.save(student);
        return CREATE_RESULT.SUCCESS;
    }

    @Override
    @Transactional
    public List<Deck> findDecksByOwner(int id) {
        Student student = findById(id);
        List<Deck> deck_ids = new ArrayList<>();

        for(Deck deck : student.getDecks())
        {
            deck_ids.add(deck);
        }
        return deck_ids;

    }

    @Override
    @Transactional
    public Student findById(int id) {
        return studentRepository.findOne(id);
    }

    @Override
    @Transactional
    public Iterable<Student> findAll() {
        return studentRepository.findAll();
    }

    @Override
    @Transactional
    public Student create(Student student) {
        Student createdStudent = student;
        return studentRepository.save(createdStudent);
    }

    @Override
    @Transactional(rollbackFor = StudentNotFound.class)
    public Student delete(int id) throws StudentNotFound {
        Student deletedStudent = studentRepository.findOne(id);
        if(deletedStudent == null)
            throw new StudentNotFound();

        studentRepository.delete(deletedStudent);
        return deletedStudent;
    }

    @Override
    @Transactional(rollbackFor = StudentNotFound.class)
    public Student update(Student student) throws StudentNotFound {
        Student updatedStudent = studentRepository.findOne(student.getId());

        if(updatedStudent == null)
            throw new StudentNotFound();

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
