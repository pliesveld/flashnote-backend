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
public interface AdminService {

    @Transactional
    void deleteStudent(int id)                        throws StudentNotFoundException;

    @Transactional
    StudentDetails update(StudentDetails studentDetails) throws StudentNotFoundException;

    @NotNull
    List<StudentDetails> findAllStudentDetails();

    @Transactional
    Student createStudent(String name, String email, String password);
}
