package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question,Integer> {
}
