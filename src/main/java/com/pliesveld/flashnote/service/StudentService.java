package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentNotFound;

import java.util.List;

public interface StudentService {
    enum CREATE_RESULT {EMAIL_TAKEN, INVALID_TOKENS, INSECURE_PASSWORD, SUCCESS }
    CREATE_RESULT       create(String name,String email,String password);

    Student             create(Student student);
    Student             delete(int id)          throws StudentNotFound;
    Student             update(Student student) throws StudentNotFound;
    Student             findById(int id);
    Long                count();
    Iterable<Student>   findAll();
    List<Deck>          findDecksByOwner(int id);
}
