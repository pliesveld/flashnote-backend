package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "STATEMENT")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractStatement extends AbstractAuditableEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "STATEMENT_ID")
    protected Integer id;

    @Column(name = "CONTENT", length = Constants.MAX_STATEMENT_CONTENT_LENGTH)
    @Size(max = Constants.MAX_STATEMENT_CONTENT_LENGTH)
    protected String content;

    public AbstractStatement() {}

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
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
        AbstractStatement other = (AbstractStatement) obj;
        if (id == null) {
            if (other.id != null) return false;
        } else if (!id.equals(other.id)) return false;
        return true;
    }

}
