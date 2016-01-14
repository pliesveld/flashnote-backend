package com.pliesveld.flashnote.model;

import javax.persistence.*;

@Entity
@Table(name = "FLASHCARD")
public class FlashCard
{

    @EmbeddedId
    private FlashCardPrimaryKey id;

    @ManyToOne(cascade = {CascadeType.ALL},
            targetEntity = com.pliesveld.flashnote.model.Question.class)
    @JoinColumn(name = "QUESTION_ID",
            nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT,name = "FK_QUESTION"))
    private Question question;

    @ManyToOne(cascade = {CascadeType.ALL},
            targetEntity = com.pliesveld.flashnote.model.Answer.class)
    @JoinColumn(name = "ANSWER_ID",
            nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(value = ConstraintMode.CONSTRAINT,name = "FK_ANSWER"))
    private Answer answer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlashCard flashCard = (FlashCard) o;

        if (!id.equals(flashCard.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    public FlashCard() {
        id = new FlashCardPrimaryKey();
    }

    public FlashCard(Integer questionId,Integer answerId) {
        id = new FlashCardPrimaryKey(questionId,answerId);
    }


    public FlashCard(Question question, Answer answer) {
        this(question.getId(), answer.getId());
        this.question = question;
        this.answer = answer;
    }


    public Question getQuestion() {
        return question;
    }


    public Answer getAnswer() {
        return answer;
    }


}