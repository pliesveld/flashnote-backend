package com.pliesveld.flashnote.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public abstract class AbstractStatement extends AbstractDatedEntity
{
    @Column(name = "CONTENT",length=65600)
    protected String content;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    protected Set<Attachment> attachments = new HashSet<>();

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

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }
}
