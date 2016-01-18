package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.dao.StudentDao;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;

import java.util.List;

/**
 * Created by happs on 1/14/16.
 */
public interface StudentService {
    enum CREATE_RESULT {EMAIL_TAKEN, INVALID_TOKENS, INSECURE_PASSWORD, SUCCESS }
    CREATE_RESULT       create(String name,String email,String password);

    Student             findById(int id);
    List<Integer>       findDecksByOwner(int id);
    Iterable<Student>   findAll();

    StudentDao getDao();
}
