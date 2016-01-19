package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.dao.StudentDao;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by happs on 1/15/16.
 */

@Service(value = "studentService")
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentDao studentDao;

    @Override
    public CREATE_RESULT create(String name, String email, String password) {
        Student student = new Student();
        student.setName(name);
        student.setEmail(email);
        student.setPassword(password);

        Student other = studentDao.getStudent(email);
        if(other != null)
            return CREATE_RESULT.EMAIL_TAKEN;


        studentDao.save(student);
        return CREATE_RESULT.SUCCESS;
    }

    @Override
    public List<Integer> findDecksByOwner(int id) {
        Student student = findById(id);
        List<Integer> deck_ids = new ArrayList<>();

        for(Deck deck : student.getDecks())
        {
            deck_ids.add(deck.getId());
        }
        return deck_ids;

    }

    @Override
    public Student findById(int id) {
        return studentDao.getDomainObjectById(id);
    }

    @Override
    @Transactional
    public Iterable<Student> findAll() {
        return studentDao.findAll();
    }

    @Override
    public StudentDao getDao() {
        return studentDao;
    }
}
