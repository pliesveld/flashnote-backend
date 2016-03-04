package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Transactional
@Validated
public interface StudentService {

    Student create(@NotNull String name,@NotNull String email,@NotNull String password) throws StudentCreateException;

    @Transactional(readOnly = true)
    Student findByEmail(@NotNull String email)                    throws StudentNotFoundException;

    //StudentDetails             create(StudentDetails studentDetails) throws StudentCreateException;
    StudentDetails delete(int id)                        throws StudentNotFoundException;
    StudentDetails update(StudentDetails studentDetails) throws StudentNotFoundException;

    @Transactional(readOnly = true)
    StudentDetails findStudentDetailsById(int id)        throws StudentNotFoundException;
    @Transactional(readOnly = true)
    Student findStudentById(int id)                      throws StudentNotFoundException;

    @Transactional(readOnly = true)
    Long                count();
    @Transactional(readOnly = true)
    Iterable<StudentDetails>   findAll();
    @Transactional(readOnly = true)
    List<Deck>          findDecksByOwner(int id);
}
