package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "QUESTION_BANK")
public class QuestionBank extends AbstractAuditableEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "QUESTION_BANK_ID")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @NotNull
    @Column(name = "DESCRIPTION", length = Constants.MAX_DECK_DESCRIPTION_LENGTH, nullable = false)
    private String description;

    @NotNull
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "QUESTION_BANK_COLLECTION",
            joinColumns = @JoinColumn(name = "QUESTION_BANK_ID"),
            inverseJoinColumns = @JoinColumn(name = "QUESTION_ID",referencedColumnName = "QUESTION_ID")
    )
    private Set<Question> questions = new HashSet<Question>();

    public QuestionBank() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public void add(@NotNull Question question) {
        this.questions.add(question);
    }
}
