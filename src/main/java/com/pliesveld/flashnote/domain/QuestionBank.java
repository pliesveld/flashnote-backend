package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;
import com.pliesveld.flashnote.schema.Constants;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Entity
@EntityListeners(value = {LogEntityListener.class})
@Table(name = "QUESTION_BANK")
//@Table(name = "QUESTION_BANK", indexes = { @Index(name = "IDX_QUESTION_BANK_OWNER_ID", columnList = "OWNER_ID") })
@NamedEntityGraph(name = "graph.QuestionBank.annotations",
    attributeNodes = @NamedAttributeNode(value = "questions", subgraph = "questions"),
    subgraphs = @NamedSubgraph(name = "questions", attributeNodes = @NamedAttributeNode("annotations")))
public class QuestionBank extends DomainBaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QUESTION_BANK_ID")
    @JsonView(Views.Summary.class)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "CATEGORY_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_QUESTION_BANK_CATEGORY_ID"))
    @LazyToOne(LazyToOneOption.PROXY)
    @JsonView(Views.Summary.class)
    private Category category;

    @NotNull
    @Size(min = Constants.MIN_DECK_DESCRIPTION_LENGTH, max = Constants.MAX_DECK_DESCRIPTION_LENGTH)
    @Column(name = "DESCRIPTION", length = Constants.MAX_DECK_DESCRIPTION_LENGTH, nullable = false)
    @JsonView(Views.Summary.class)
    private String description;

    @NotNull
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinTable(name = "QUESTION_BANK_COLLECTION",
            joinColumns = @JoinColumn(name = "QUESTION_BANK_ID", foreignKey = @ForeignKey(name = "FK_QUESTION_BANK_COLLECTION_QUESTION_BANK_ID")),
            inverseJoinColumns = @JoinColumn(name = "QUESTION_ID", nullable = false,
                    referencedColumnName = "QUESTION_ID",
                    foreignKey = @ForeignKey(name = "FK_QUESTION_BANK_COLLECTION_QUESTION_ID"))

//            foreignKey = @ForeignKey(name = "FK_QUESTION_BANK_COLLECTION_QUESTION_BANK_ID"),
//            inverseForeignKey= @ForeignKey(name = "FK_QUESTION_BANK_COLLECTION_QUESTION_BANK_ID"),
    )
    @LazyCollection(LazyCollectionOption.EXTRA)
    @JsonView(Views.SummaryWithCollections.class)
    private Set<Question> questions = new HashSet<Question>();

    @NotNull
    @Column(name = "OWNER_ID")
    @Basic(optional = false)
    int owner;

    protected QuestionBank() {
        super();
    }

    public QuestionBank(@NotNull Category category, @NotNull String description) {
        this();
        this.category = category;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    //    @JsonSerialize(using = DomainObjectSerializer.class)
    public Category getCategory() {
        return category;
    }

    //    @JsonDeserialize(using = CategoryDeserializer.class)
    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //    @JsonSerialize(contentUsing = DomainObjectSerializer.class)
    public Set<Question> getQuestions() {
        return questions;
    }

    //    @JsonDeserialize(contentUsing = QuestionSetDeserializer.class)
    protected void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public void add(@NotNull Question question) {
        this.questions.add(question);
    }
}
