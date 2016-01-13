package com.pliesveld.flashnote.model;

import javax.persistence.Column;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractStatement
{
    @Column(name = "CONTENT",length=65600)
    protected String content;

    public AbstractStatement() {
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
