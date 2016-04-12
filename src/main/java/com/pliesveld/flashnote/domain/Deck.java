package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



@Entity
@Table(name = "DECK",
        indexes = { @Index(name = "IDX_DECK_AUTHOR_ID",
                           columnList = "STUDENT_ID") })
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
    @JoinColumn(name = "STUDENT_ID", foreignKey = @ForeignKey(name = "FK_DECK_AUTHOR"), nullable = false)
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
    @JsonProperty("flashcards")
    private List<FlashCard> flashCards = new ArrayList<>();

    @NotNull
    @Column(name = "DECK_DESCRIPTION", length = Constants.MAX_DECK_DESCRIPTION_LENGTH, nullable = false)
    private String description = "Untitled";

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", foreignKey = @ForeignKey(name = "FK_DECK_CATEGORY"), nullable = false)
    private Category category;

    public Deck() {}

    public Deck(StudentDetails author) {
        setAuthor(author);
    }

    public Deck(StudentDetails author, FlashCard... cards) {
        this(author);
        for(FlashCard fc : cards)
        {
            flashCards.add(fc);
        }
    }

    public Deck(StudentDetails author, String description, FlashCard... flashCards) {
        this(author, flashCards);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deck deck = (Deck) o;

        if (author != null ? !author.equals(deck.author) : deck.author != null) return false;
        if (category != null ? !category.equals(deck.category) : deck.category != null) return false;
        if (description != null ? !description.equals(deck.description) : deck.description != null) return false;
        if (flashCards != null ? !flashCards.equals(deck.flashCards) : deck.flashCards != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = author != null ? author.hashCode() : 0;
        result = 31 * result + (flashCards != null ? flashCards.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }
}
