package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.StudentDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentDetailsRepository extends CrudRepository<StudentDetails,Integer> {

}
