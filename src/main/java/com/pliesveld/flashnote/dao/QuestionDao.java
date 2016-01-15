package com.pliesveld.flashnote.dao;

import com.pliesveld.flashnote.domain.Question;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class QuestionDao extends GenericDao<Question> {
    public QuestionDao() {
        setPersistentClass(Question.class);
    }
}

