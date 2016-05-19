package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;
import com.pliesveld.flashnote.schema.Constants;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Entity
@EntityListeners(value = { LogEntityListener.class })
@Table(name = "DECK",
        indexes = { @Index(name = "IDX_DECK_OWNER_ID",
                           columnList = "OWNER_ID") })
public class Deck extends DomainBaseEntity<Integer> implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "DECK_ID")
    @JsonView(Views.Summary.class)
    private Integer id;

    @OneToMany(cascade = {CascadeType.ALL})
//    @OrderColumn(name = "FLASHCARD_ORDER",updatable=false)
    @JoinTable(name = "DECK_FLASHCARD",
            foreignKey = @ForeignKey(name = "FK_DECK"),

            joinColumns = {@JoinColumn(name = "DECK_ID", foreignKey = @ForeignKey(name = "FK_DECK_FLASHCARD_DECK_ID"))},

            inverseForeignKey = @ForeignKey(name = "FK_FLASHCARD"),
            inverseJoinColumns = {
                            @JoinColumn(table = "QUESTION",  name = "QUESTION_ID", foreignKey= @ForeignKey(name = "FK_DECK_FLASHCARD_QUESTION_ID")),
                            @JoinColumn(table = "ANSWER",    name = "ANSWER_ID",   foreignKey= @ForeignKey(name = "FK_DECK_FLASHCARD_ANSWER_ID"))
            },
            uniqueConstraints = {@UniqueConstraint(name="UNIQUE_FLASHCARD",columnNames = {"QUESTION_ID","ANSWER_ID"})}

    )
    @JsonView(Views.SummaryWithCollections.class)
//    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<FlashCard> flashcards = new ArrayList<>();

    @NotNull
    @Size(min = Constants.MIN_DECK_DESCRIPTION_LENGTH, max = Constants.MAX_DECK_DESCRIPTION_LENGTH)
    @Column(name = "DECK_DESCRIPTION", length = Constants.MAX_DECK_DESCRIPTION_LENGTH, nullable = false)
    @JsonView(Views.Summary.class)
    private String description = "Untitled";

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CATEGORY_ID", foreignKey = @ForeignKey(name = "FK_DECK_CATEGORY_ID"), nullable = false)
    @LazyToOne(LazyToOneOption.PROXY)
    @JsonView(Views.Summary.class)
    private Category category;

    @NotNull
    @Column(name = "OWNER_ID")
    @Basic(optional = false)
    int owner;

    protected Deck() {
        super();
    }

    public Deck(FlashCard... cards) {
        for(FlashCard fc : cards)
        {
            flashcards.add(fc);
        }
    }

    public Deck(String description, FlashCard... flashcards) {
        this(flashcards);
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
