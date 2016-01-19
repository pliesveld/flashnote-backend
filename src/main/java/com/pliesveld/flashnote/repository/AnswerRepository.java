package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer,Integer> {
}
