package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionBankRepository extends JpaRepository<QuestionBank,Integer>, JpaSpecificationExecutor<QuestionBank> {
    List<QuestionBank> findByOwner(int id);

    List<QuestionBank> findByQuestionsContaining(final int questionId);
}