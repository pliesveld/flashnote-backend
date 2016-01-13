package com.pliesveld.flashnote.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by happs on 1/13/16.
 */

@Embeddable
public class FlashCardPrimaryKey implements Serializable {

    @Column(name = "QUESTION_ID",nullable = false)
    private Integer questionId;

    @Column(name = "ANSWER_ID",nullable = false)
    private Integer answerId;

    public FlashCardPrimaryKey() {
    }

    public FlashCardPrimaryKey(Integer questionId, Integer answerId) {
        this.questionId = questionId;
        this.answerId = answerId;
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public Integer getAnswerId() {
        return answerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlashCardPrimaryKey id = (FlashCardPrimaryKey) o;

        return answerId.equals(id.answerId) &&
                questionId.equals(id.questionId);
    }

    @Override
    public int hashCode() {
        if(questionId == null || answerId == null)
            return 0;
        return answerId.hashCode() + questionId.hashCode();
    }
}

