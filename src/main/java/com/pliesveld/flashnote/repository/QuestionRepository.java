package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.repository.base.AbstractStatementRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends AbstractStatementRepository<Question>, JpaRepository<Question, Integer> {
    List<Question> findByAttachmentId(int id);
}
