package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    Student findOneByEmail(final String email);

    @Query("select s from Student s")
    Stream<Student> findAllAsStream();

    Student findByName(final String name);
}
