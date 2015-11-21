package com.pliesveld.flashnote.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;;

@Entity
@Table(name = "DECK")
public class Deck implements Serializable
{
    @Id
    @GeneratedValue
    @Column(name = "DECK_ID")
    private Short id;

    @OneToMany(orphanRemoval = true)
    @OrderColumn(name = "FLASHCARD_ORDER")
    @JoinTable(name = "DECK_FLASHCARD", joinColumns = @JoinColumn(name = "DECK_ID") , inverseJoinColumns = @JoinColumn(name = "FLASHCARD_ID") )
    private List<FlashCard> flashCards;

    public Short getId()
    {
        return id;
    }

    public void setId(Short id)
    {
        this.id = id;
    }

}
