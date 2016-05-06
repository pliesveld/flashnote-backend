package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import java.util.Objects;

@Entity
@EntityListeners(value = { LogEntityListener.class })
@Table(name = "FLASHCARD")
public class FlashCard extends DomainBaseEntity<FlashCardPrimaryKey> implements Comparable<FlashCard>
{
    private static final Logger LOG = LogManager.getLogger();

    @EmbeddedId
    private FlashCardPrimaryKey id = new FlashCardPrimaryKey();

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            targetEntity = com.pliesveld.flashnote.domain.Question.class)
    @JoinColumn(name = "QUESTION_ID",
            nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_QUESTION"))
    @MapsId("questionId")
    @LazyToOne(LazyToOneOption.PROXY)
    private Question question;

    @ManyToOne(cascade = {CascadeType.ALL},
            targetEntity = com.pliesveld.flashnote.domain.Answer.class)
    @JoinColumn(name = "ANSWER_ID",
            nullable = false, insertable = false, updatable = false,
            foreignKey = @ForeignKey(name = "FK_ANSWER"))
    @MapsId("answerId")
    @LazyToOne(LazyToOneOption.PROXY)
    private Answer answer;


    public FlashCard() {
        super();
    }

    public FlashCard(Integer questionId, Integer answerId) {
        this();
        /*
        if(questionId == null || answerId == null)
        {
            throw new NullPointerException(String.format("Cannot pass a null %s",questionId == answerId ? "question or answer" : questionId == null ? "question" : "answer"));
        }
        */

        id.setQuestionId(questionId);
        id.setAnswerId(answerId);
    }

    @JsonView(Views.Summary.class)
    public FlashCardPrimaryKey getId()        { return id; }

    public void setId(FlashCardPrimaryKey id) {
        this.id = id;
    }

    public FlashCard(Question question, Answer answer) {
        this(question.getId(), answer.getId());

        this.question = question;
        this.answer = answer;
    }

    @JsonView(Views.Summary.class)
    public Question getQuestion()             { return question; }

    @JsonView(Views.Summary.class)
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