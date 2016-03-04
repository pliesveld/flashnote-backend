package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "DECK")
@NamedQueries(
        @NamedQuery(name = "Deck.count", query = "SELECT COUNT(d) FROM Deck d")
)
@EntityListeners(value = { LogEntityListener.class })
public class Deck extends DomainBaseEntity implements Serializable
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "DECK_ID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private StudentDetails author;

    @OneToMany(cascade = {CascadeType.ALL})
//    @OrderColumn(name = "FLASHCARD_ORDER",updatable=false)
    @JoinTable(name = "DECK_FLASHCARD",
            foreignKey = @ForeignKey(name = "FK_DECK"),

            joinColumns = {@JoinColumn(name = "DECK_ID")},

            inverseForeignKey = @ForeignKey(name = "FK_FLASHCARD"),
            inverseJoinColumns = {
                            @JoinColumn(table = "QUESTION",  name = "QUESTION_ID", foreignKey= @ForeignKey(name="FK_DECK_FLASHCARD_QUESTION_ID")),
                            @JoinColumn(table = "ANSWER",    name = "ANSWER_ID",   foreignKey= @ForeignKey(name="FK_DECK_FLASHCARD_ANSWER_ID"))
            }
            /*,uniqueConstraints = {@UniqueConstraint(name="UNIQUE_FLASHCARD",columnNames = {"QUESTION_ID","ANSWER_ID"})}*/

    )
    private List<FlashCard> flashCards = new ArrayList<>();

    @Column(name = "DECK_TITLE", length=177)
    private String title = "Untitled";

    @ManyToOne
    private Category category;

    public Deck(StudentDetails author) {
        this.author = author;
    }

    public Deck(StudentDetails author, FlashCard... cards) {
        this.author = author;
        for(FlashCard fc : cards)
        {
            flashCards.add(fc);
        }
    }

    public Deck(StudentDetails author, String title, FlashCard... flashCards) {
        this(author, flashCards);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public List<FlashCard> getFlashCards() {
        return flashCards;
    }

    public void setFlashCards(List<FlashCard> flashCards) {
        this.flashCards = flashCards;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public StudentDetails getAuthor() {
        return author;
    }

    public void setAuthor(StudentDetails author) {
        this.author = author;
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

    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }
}
