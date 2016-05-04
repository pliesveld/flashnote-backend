package com.pliesveld.flashnote.domain;

import org.hibernate.proxy.HibernateProxy;

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

    public final Integer getId() {
        if (this instanceof HibernateProxy) {
            return (Integer)((HibernateProxy)this).getHibernateLazyInitializer().getIdentifier();
        }
        else { return id; }
    }

    public Question() {
        super();
    }

    public Question(String content) {
        this();
        setContent(content);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
