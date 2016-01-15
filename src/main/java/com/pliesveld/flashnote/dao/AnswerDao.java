package com.pliesveld.flashnote.dao;

import com.pliesveld.flashnote.domain.Answer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class AnswerDao extends GenericDao<Answer> {
    public AnswerDao() {
        setPersistentClass(Answer.class);
    }
}

