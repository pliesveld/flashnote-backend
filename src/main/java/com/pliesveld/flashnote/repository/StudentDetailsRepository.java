package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.StudentDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface StudentDetailsRepository extends PagingAndSortingRepository<StudentDetails,Integer> {

    StudentDetails findByName(String name);

    @Query("select s from StudentDetails s")
    Stream<StudentDetails> findAllAsStream();
}
