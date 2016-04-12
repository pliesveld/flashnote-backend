package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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
    private List<FlashCard> flashcards = new ArrayList<>();

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
            flashcards.add(fc);
        }
    }

    public Deck(StudentDetails author, String description, FlashCard... flashcards) {
        this(author, flashcards);
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

    public List<FlashCard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(List<FlashCard> flashcards) {
        this.flashcards = flashcards;
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
    public int hashCode() {
        return Objects.hash(description);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof Deck)) {
            return false;
        }
        final Deck other = (Deck) obj;
        return Objects.equals(getDescription(), other.getDescription());
    }
}
