package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface StudentService {

    Student             create(String name,String email,String password) throws StudentCreateException;

    Student findByEmail(String email);

    //Student             create(Student student) throws StudentCreateException;
    Student             delete(int id)          throws StudentNotFoundException;
    Student             update(Student student) throws StudentNotFoundException;
    Student             findById(int id)        throws StudentNotFoundException;
    Long                count();
    Iterable<Student>   findAll();
    List<Deck>          findDecksByOwner(int id);
}
