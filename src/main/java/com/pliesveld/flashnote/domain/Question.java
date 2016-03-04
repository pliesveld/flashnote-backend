package com.pliesveld.flashnote.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "QUESTION")
@PrimaryKeyJoinColumn(name = "QUESTION_ID", foreignKey = @ForeignKey(name = "FK_QUESTION_ID"))
public class Question extends AbstractStatement implements Serializable
{
    @Column(name = "QUESTION_TITLE")
    private String title;

    @PrePersist
    public void prePersist() {
        if(title == null)
            title = "Untitled Question";
    }

    public Question() {
    }

    public Question(String content) {
        setContent(content);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
