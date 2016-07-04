package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.exception.StudentNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;


@Validated
@Transactional(readOnly = true)
public interface AdminService {

    @Transactional
    void deleteStudent(final int id) throws StudentNotFoundException;

    @Transactional
    Student update(final Student student) throws StudentNotFoundException;

    @NotNull
    List<Student> findAllStudent();

    @Transactional
    Student createStudent(final String name, final String email, final String password);
}
