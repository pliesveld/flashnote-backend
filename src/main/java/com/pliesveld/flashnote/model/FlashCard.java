package com.pliesveld.flashnote.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "FLASHCARD", uniqueConstraints = { @UniqueConstraint(columnNames = { "QUESTION_ID", "ANSWER_ID" }) })
public class FlashCard
{
    @Id
    @Column(name = "FLASHCARD_ID")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "QUESTION_ID", nullable = false)
    private Question question;

    @ManyToOne
    @JoinColumn(name = "ANSWER_ID", nullable = false)
    private Answer answer;

    public FlashCard() {
    }

}