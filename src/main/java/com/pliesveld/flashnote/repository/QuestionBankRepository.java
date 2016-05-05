package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.QuestionBank;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBankRepository extends CrudRepository<QuestionBank,Integer> {
    List<QuestionBank> findByOwner(int id);
}