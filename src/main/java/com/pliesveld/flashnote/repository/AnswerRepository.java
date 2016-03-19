package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.repository.base.AbstractStatementRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends AbstractStatementRepository<Answer>, CrudRepository<Answer,Integer> {
}
