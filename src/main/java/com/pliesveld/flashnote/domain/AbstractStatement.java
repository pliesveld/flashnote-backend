package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.domain.base.AbstractAuditableEntity;
import com.pliesveld.flashnote.schema.Constants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

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
    @Column(name = "CONTENT", length = Constants.MAX_STATEMENT_CONTENT_LENGTH, nullable = false)
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
    public int hashCode() {
        return Objects.hash(content);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof AbstractStatement)) {
            return false;
        }
        final AbstractStatement other = (AbstractStatement) obj;
        return Objects.equals(getContent(), other.getContent());
    }
}
