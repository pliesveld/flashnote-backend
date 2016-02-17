package com.pliesveld.flashnote.domain;

import javax.persistence.*;

import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;

@Entity
@EntityListeners(value = { LogEntityListener.class })
@Table(name = "FLASHCARD")
@NamedQueries(
        @NamedQuery(name = "FlashCard.count", query = "SELECT COUNT(f) FROM FlashCard f")
)
public class FlashCard implements Comparable<FlashCard>
{
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();


    @EmbeddedId
    private FlashCardPrimaryKey id;

    @ManyToOne(cascade = {CascadeType.ALL},
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


    @PrePersist
    public void debugIdPre()
    {
        LOG.debug("prepersist; id = " + this.id);
        LOG.debug("prepersist; id = " + this.getId());
    }
    
    @PostPersist
    public void debugIdPost()
    {
        LOG.debug("postpersist; id = " + this.id);
        LOG.debug("postpersist; id = " + this.getId());
    }
}