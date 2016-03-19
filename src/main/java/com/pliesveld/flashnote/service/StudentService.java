package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AbstractStatement;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;


@Validated
@Transactional(readOnly = true)
public interface StudentService {

    @NotNull
    @Transactional
    Student create(@NotNull String name,@NotNull String email,@NotNull String password) throws StudentCreateException;

    @NotNull
    Student findByEmail(@NotNull String email)                    throws StudentNotFoundException;

    @NotNull
    @Transactional
    StudentDetails delete(int id)                        throws StudentNotFoundException;

    @NotNull
    @Transactional
    StudentDetails update(StudentDetails studentDetails) throws StudentNotFoundException;

    @NotNull
    StudentDetails findStudentDetailsById(int id)        throws StudentNotFoundException;

    @NotNull
    Student findStudentById(int id)                      throws StudentNotFoundException;

    @NotNull
    Long                count();

    @NotNull
    Iterable<StudentDetails>   findAll();

    @NotNull
    List<Deck>          findDecksByOwner(int id);

    @NotNull
    List<AbstractStatement> findPublishedStatementsBy(StudentDetails studentDetails);
}
