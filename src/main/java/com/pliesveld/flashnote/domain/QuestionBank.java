package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;

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
    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", nullable = false)
    private Category category;

    @NotNull
    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @NotNull
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "QUESTION_BANK_COLLECTION",
            joinColumns = @JoinColumn(name = "QUESTION_BANK_ID"),
            inverseJoinColumns = @JoinColumn(name = "QUESTION_ID",referencedColumnName = "QUESTION_ID")
    )
    private Set<Question> questionCollection = new HashSet<Question>();

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

    public Set<Question> getQuestionCollection() {
        return questionCollection;
    }

    public void setQuestionCollection(Set<Question> questionCollection) {
        this.questionCollection = questionCollection;
    }


    public void add(@NotNull Question question) {
        this.questionCollection.add(question);
    }
}
