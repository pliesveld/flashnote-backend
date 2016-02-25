package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface StudentService {

    Student create(String name, String email, String password) throws StudentCreateException;

    Student findByEmail(String email)                        throws StudentNotFoundException;

    //StudentDetails             create(StudentDetails studentDetails) throws StudentCreateException;
    StudentDetails delete(int id)          throws StudentNotFoundException;
    StudentDetails update(StudentDetails studentDetails) throws StudentNotFoundException;
    StudentDetails findById(int id)        throws StudentNotFoundException;
    Long                count();
    Iterable<StudentDetails>   findAll();
    List<Deck>          findDecksByOwner(int id);
}
