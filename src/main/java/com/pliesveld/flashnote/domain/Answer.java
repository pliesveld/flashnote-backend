package com.pliesveld.flashnote.domain;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ANSWER")
public class Answer extends AbstractStatement implements Serializable
{
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ANSWER_ID")
    protected Integer id;

    public Answer() {
    }

    public Answer(String content) {
        setContent(content);
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        Answer other = (Answer) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

}
