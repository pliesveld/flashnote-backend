package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.model.json.Views;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Embeddable
public class FlashCardPrimaryKey implements Serializable {

    @NotNull
    @Column(name = "QUESTION_ID", nullable = false)
    private Integer questionId;

    @NotNull
    @Column(name = "ANSWER_ID", nullable = false)
    private Integer answerId;

    public FlashCardPrimaryKey() {
    }

    public FlashCardPrimaryKey(Integer questionId, Integer answerId) {
        setQuestionId(questionId);
        setAnswerId(answerId);
    }

    @JsonView(Views.Summary.class)
    public Integer getQuestionId() {
        return questionId;
    }

    @JsonView(Views.Summary.class)
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
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        FlashCardPrimaryKey id = (FlashCardPrimaryKey) other;

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
    public String toString() {
        return "FlashCardPrimaryKey [questionId=" + questionId + ", answerId=" + answerId + "]";
    }

}

