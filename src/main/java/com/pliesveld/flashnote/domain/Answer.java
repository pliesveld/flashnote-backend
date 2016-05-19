package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@EntityListeners(value = { LogEntityListener.class })
@Table(name = "ANSWER")
@PrimaryKeyJoinColumn(name = "ANSWER_ID", foreignKey = @ForeignKey(name = "FK_ANSWER_ID"))
public class Answer extends AbstractStatement implements Serializable
{
    public Answer() {
        super();
    }

    public Answer(String content) {
        this();
        setContent(content);
    }

}
