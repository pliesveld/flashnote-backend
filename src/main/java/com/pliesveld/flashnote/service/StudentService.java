package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;

import java.util.List;

public interface StudentService {
    enum CREATE_RESULT {EMAIL_TAKEN, INVALID_TOKENS, INSECURE_PASSWORD, SUCCESS }
    CREATE_RESULT       create(String name,String email,String password);

    Student             create(Student student) throws StudentCreateException;
    Student             delete(int id)          throws StudentNotFoundException;
    Student             update(Student student) throws StudentNotFoundException;
    Student             findById(int id)        throws StudentNotFoundException;
    Long                count();
    Iterable<Student>   findAll();
    List<Deck>          findDecksByOwner(int id);
}
