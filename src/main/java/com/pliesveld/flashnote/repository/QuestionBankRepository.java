package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.repository.base.AbstractStatementRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionBankRepository extends CrudRepository<QuestionBank,Integer> {
}
