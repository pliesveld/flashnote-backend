package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@EntityListeners(value = { LogEntityListener.class })
@Table(name = "QUESTION")
@PrimaryKeyJoinColumn(name = "QUESTION_ID", foreignKey = @ForeignKey(name = "FK_QUESTION_ID"))
public class Question extends AbstractStatement implements Serializable
{
    protected String title;
    protected AbstractAttachment attachment;

    public Question() {
        super();
    }

    public Question(String content) {
        this();
        setContent(content);
    }

    @Column(name = "QUESTION_TITLE")
    @JsonView(Views.Summary.class)
    public String getTitle() {
        return title;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true, targetEntity = AbstractAttachment.class)
    @JoinColumn(name = "ATTACHMENT_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_QUESTION_ATTACHMENT_ID"))
    @JsonView(Views.Summary.class)
    public <T extends AbstractAttachment> T getAttachment() {
        return (T) attachment;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public <T extends AbstractAttachment> void setAttachment(T attachment) {
        this.attachment = attachment;
    }

    @PrePersist
    public void prePersist() {
        if(title == null)
            title = "Untitled Question";
    }

}
