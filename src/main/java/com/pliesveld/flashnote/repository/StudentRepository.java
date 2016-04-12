package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface StudentRepository extends CrudRepository<Student,Integer> {
    Student findOneByEmail(String email);

    @Query("select s from Student s")
    Stream<Student> findAllAsStream();
}