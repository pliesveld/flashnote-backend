package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student,Integer> {
    Student findOneByEmail(String email);
}
