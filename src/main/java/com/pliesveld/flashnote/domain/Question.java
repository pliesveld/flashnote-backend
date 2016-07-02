package com.pliesveld.flashnote.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.pliesveld.flashnote.model.json.Views;
import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@EntityListeners(value = {LogEntityListener.class})
@Table(name = "QUESTION")
@DiscriminatorValue(value = "QUESTION")
@PrimaryKeyJoinColumn(name = "QUESTION_ID", foreignKey = @ForeignKey(name = "FK_QUESTION_ID"))
public class Question extends AbstractStatement implements Serializable {
    protected AbstractAttachment attachment;

    public Question() {
        super();
    }

    public Question(String content) {
        this();
        setContent(content);
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true, targetEntity = AbstractAttachment.class)
    @JoinColumn(name = "ATTACHMENT_ID", nullable = true, foreignKey = @ForeignKey(name = "FK_QUESTION_ATTACHMENT_ID"))
    @JsonView(Views.Summary.class)
    @SuppressWarnings("unchecked")
    public <T extends AbstractAttachment> T getAttachment() {
        return (T) attachment;
    }

    public <T extends AbstractAttachment> void setAttachment(T attachment) {
        this.attachment = attachment;
    }

}
