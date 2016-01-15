package com.pliesveld.flashnote.dao;


import com.pliesveld.flashnote.domain.Question;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class FlashCardDao extends GenericDao<Question> {
    public FlashCardDao() {
        setPersistentClass(Question.class);
    }
}

