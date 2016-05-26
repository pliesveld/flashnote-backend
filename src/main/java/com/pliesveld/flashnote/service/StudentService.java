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
    StudentDetails delete(final int id)                        throws StudentNotFoundException;

    StudentDetails findStudentDetailsById(final int id)        throws StudentNotFoundException;

    Student findStudentById(final int id)                      throws StudentNotFoundException;

    @NotNull
    Long                count();

    @NotNull
    Page<StudentDetails> findAll(final Pageable page);

    @NotNull
    List<Deck>          findDecksByOwner(final int id);

    @NotNull
    List<AbstractStatement> findStatementsBy(final StudentDetails studentDetails);

}
