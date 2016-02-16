package com.pliesveld.flashnote.domain;

import javax.persistence.*;

@Entity
@Table(name = "FLASHCARD")
@NamedQueries(
        @NamedQuery(name = "FlashCard.count", query = "SELECT COUNT(f) FROM FlashCard f")
)
public class FlashCard implements Comparable<FlashCard>
{

    @EmbeddedId
    private FlashCardPrimaryKey id;

    @ManyToOne(cascade = {CascadeType.ALL},
            targetEntity = com.pliesveld.flashnote.domain.Question.class)
    @JoinColumn(name = "QUESTION_ID",
            nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_QUESTION"))
    private Question question;

    @ManyToOne(cascade = {CascadeType.ALL},
            targetEntity = com.pliesveld.flashnote.domain.Answer.class)
    @JoinColumn(name = "ANSWER_ID",
            nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_ANSWER"))
    private Answer answer;


    public FlashCard() {
        id = new FlashCardPrimaryKey();
    }

    public FlashCard(Integer questionId,Integer answerId) {
        id = new FlashCardPrimaryKey(questionId,answerId);
    }

    public FlashCardPrimaryKey getId() {
        return id;
    }

    public void setId(FlashCardPrimaryKey id) {
        this.id = id;
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

    @Override
    public int compareTo(FlashCard o) { // TODO: move logic to FlashCardPrimaryKey
        if(id == null || o.id == null)
        {
           return 1;
        }
        
        if(id.getAnswerId() == null || id.getAnswerId() == null)
        {
            return 1;
        }
        
        if(o.id.getAnswerId() == null || o.id.getAnswerId() == null)
        {
            return -1;
        }
        
        if(id.getQuestionId().equals(o.id.getQuestionId()))
        {
            return id.getAnswerId().compareTo(o.id.getAnswerId());
        }
        
        return id.getQuestionId().compareTo(o.id.getQuestionId());
    }


}