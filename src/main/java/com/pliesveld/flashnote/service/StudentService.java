package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AbstractStatement;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;


@Validated
@Transactional(readOnly = true)
public interface StudentService {

    Student findByEmail(@NotNull String email);
    StudentDetails findByName(@NotNull String name);

    @Transactional
    StudentDetails delete(int id)                        throws StudentNotFoundException;

    @Transactional
    StudentDetails update(StudentDetails studentDetails) throws StudentNotFoundException;

    StudentDetails findStudentDetailsById(int id)        throws StudentNotFoundException;

    Student findStudentById(int id)                      throws StudentNotFoundException;

    @NotNull
    Long                count();

    @NotNull
    Page<StudentDetails> findAll(Pageable page);

    @NotNull
    List<Deck>          findDecksByOwner(int id);

    @NotNull
    List<AbstractStatement> findStatementsBy(StudentDetails studentDetails);

}
