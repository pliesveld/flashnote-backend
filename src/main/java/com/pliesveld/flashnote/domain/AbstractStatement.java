package com.pliesveld.flashnote.domain;

import javax.persistence.Column;
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