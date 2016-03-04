package com.pliesveld.flashnote.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * Created by happs on 1/13/16.
 */

@Embeddable
public class FlashCardPrimaryKey implements Serializable, Comparable<FlashCardPrimaryKey> {

    @Column(name = "QUESTION_ID",nullable = false)
    private Integer questionId;

    @Column(name = "ANSWER_ID",nullable = false)
    private Integer answerId;

    public FlashCardPrimaryKey() {
    }

    public FlashCardPrimaryKey(Integer questionId, Integer answerId) {
        setQuestionId(questionId);
        setAnswerId(answerId);
    }

    public Integer getQuestionId() {
        return questionId;
    }

    public Integer getAnswerId() {
        return answerId;
    }
       
    protected void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    protected void setAnswerId(Integer answerId) {
        this.answerId = answerId;
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
        int result = questionId != null ? questionId.hashCode() : 0;
        result = 31 * result + (answerId != null ? answerId.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(FlashCardPrimaryKey o) {
        if(this.questionId == null || this.answerId == null)
        {
            if(o.questionId == null || o.answerId == null)
                return 0;
            return -1;
        }
        
        if(o.questionId == null || o.answerId == null)
            return 1;
        
        if(this.questionId.equals(o.questionId))
            return this.answerId.compareTo(o.answerId);
        return this.questionId.compareTo(o.questionId);
    }

    @Override
    public String toString() {
        return "FlashCardPrimaryKey [questionId=" + questionId + ", answerId=" + answerId + "]";
    }
    
}

