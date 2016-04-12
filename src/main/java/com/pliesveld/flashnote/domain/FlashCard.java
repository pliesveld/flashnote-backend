package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@EntityListeners(value = { LogEntityListener.class })
@Table(name = "FLASHCARD")
@NamedQueries(
        @NamedQuery(name = "FlashCard.count", query = "SELECT COUNT(f) FROM FlashCard f")
)
public class FlashCard extends DomainBaseEntity implements Comparable<FlashCard>
{
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();


    @EmbeddedId
    private FlashCardPrimaryKey id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            targetEntity = com.pliesveld.flashnote.domain.Question.class)
    @JoinColumn(name = "QUESTION_ID",
            nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_QUESTION"))
    @MapsId("questionId")
    private Question question;

    @ManyToOne(cascade = {CascadeType.ALL},
            targetEntity = com.pliesveld.flashnote.domain.Answer.class)
    @JoinColumn(name = "ANSWER_ID",
            nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_ANSWER"))
    @MapsId("answerId")
    private Answer answer;


    public FlashCard() {
        id = new FlashCardPrimaryKey();
    }

    public FlashCard(Integer questionId,Integer answerId) {
        /*
        if(questionId == null || answerId == null)
        {
            throw new NullPointerException(String.format("Cannot pass a null %s",questionId == answerId ? "question or answer" : questionId == null ? "question" : "answer"));
        }
        */

        id = new FlashCardPrimaryKey(questionId,answerId);
    }

    public FlashCardPrimaryKey getId()        { return id; }

    public void setId(FlashCardPrimaryKey id) {
        this.id = id;
    }

    public FlashCard(Question question, Answer answer) {
        this(question.getId(), answer.getId());

        this.question = question;
        this.answer = answer;
    }

    public Question getQuestion()             { return question; }

    public Answer getAnswer()                 { return answer; }

    @Override
    public int hashCode() {
        return Objects.hash(question, answer);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof FlashCard)) {
            return false;
        }
        final FlashCard other = (FlashCard) obj;
        return Objects.equals(getQuestion(), other.getQuestion()) && Objects.equals(getAnswer(), other.getAnswer());
    }

    @Override
    public int compareTo(FlashCard o) {
        if(this.id == null)
        {
            if(o.id == null)
                return 0;
            return 1;
        }
        
        if(o.id == null)
        {
            return -1;
        }
        
        return this.id.compareTo(o.id);
    }
}