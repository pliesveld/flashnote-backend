package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.StudentDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDetailsRepository extends PagingAndSortingRepository<StudentDetails,Integer> {

    StudentDetails findByName(String name);

}
