package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @Column(name = "CONTENT", length = Constants.MAX_STATEMENT_CONTENT_LENGTH)
    @Size(max = Constants.MAX_STATEMENT_CONTENT_LENGTH)
    protected String content = "";

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractStatement)) return false;

        AbstractStatement that = (AbstractStatement) o;

        if (!content.equals(that.content)) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }
}
