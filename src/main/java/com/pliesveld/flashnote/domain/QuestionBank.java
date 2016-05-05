package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
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
@Table(name = "QUESTION_BANK",
        indexes = { @Index(name = "IDX_QUESTION_BANK_OWNER_ID",
                columnList = "OWNER_ID") })
@EntityListeners(value = { LogEntityListener.class })
public class QuestionBank extends AbstractAuditableEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QUESTION_BANK_ID")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_QUESTION_BANK_CATEGORY_ID"))
    @LazyToOne(LazyToOneOption.PROXY)
    private Category category;

    @NotNull
    @Size(min = Constants.MIN_DECK_DESCRIPTION_LENGTH, max = Constants.MAX_DECK_DESCRIPTION_LENGTH)
    @Column(name = "DESCRIPTION", length = Constants.MAX_DECK_DESCRIPTION_LENGTH, nullable = false)
    private String description;

    @NotNull
    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "QUESTION_BANK_COLLECTION", foreignKey = @ForeignKey(name = "FK_QUESTION_BANK_COLLECTION_QUESTION_BANK_ID"),
            joinColumns = @JoinColumn(name = "QUESTION_BANK_ID", foreignKey = @ForeignKey(name = "QUESTION_BANK_WHAT")),
            inverseJoinColumns = @JoinColumn(name = "QUESTION_ID",
                                             referencedColumnName = "QUESTION_ID",
                                             foreignKey = @ForeignKey(name = "FK_QUESTION_BANK_COLLECTION_QUESTION_ID"))
    )
    @LazyCollection(LazyCollectionOption.EXTRA)
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
