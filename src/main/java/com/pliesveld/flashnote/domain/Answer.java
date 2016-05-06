package com.pliesveld.flashnote.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
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
