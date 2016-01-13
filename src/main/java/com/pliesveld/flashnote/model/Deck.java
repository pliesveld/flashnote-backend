package com.pliesveld.flashnote.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.persistence.*;
;

@Entity
@Table(name = "DECK")
public class Deck implements Serializable
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "DECK_ID")
    private Short id;

    @OneToMany(cascade = {CascadeType.ALL})
    @OrderColumn(name = "FLASHCARD_ORDER")
    @JoinTable(name = "DECK_FLASHCARD",
            foreignKey = @ForeignKey(name = "FK_DECK"),

            joinColumns = {@JoinColumn(name = "DECK_ID")},

            inverseForeignKey = @ForeignKey(name = "FK_FLASHCARD"),
            inverseJoinColumns = {
                            @JoinColumn(name = "QUESTION_ID"),
                            @JoinColumn(name = "ANSWER_ID")
            }
    )
    private List<FlashCard> flashCards = new ArrayList<>();

    @Column(name="DECK_TITLE", length=177)
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Short getId()
    {
        return id;
    }

    public void setId(Short id)
    {
        this.id = id;
    }

    public List<FlashCard> getFlashCards() {
        return flashCards;
    }

    public void setFlashCards(List<FlashCard> flashCards) {
        this.flashCards = flashCards;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Deck other = (Deck) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

}
