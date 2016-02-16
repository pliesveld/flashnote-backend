package com.pliesveld.flashnote.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.pliesveld.flashnote.persistence.entities.listeners.LogEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
@EntityListeners(value = { LogEntityListener.class })
public abstract class AbstractStatement
{
    @Column(name = "CONTENT",length=65600)
    protected String content;

    @Column(name = "CREATED")
    protected Instant createdOn;

    @Column(name = "MODIFIED")
    protected Instant modifiedOn;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    protected Set<Attachment> attachments = new HashSet<>();

    @PrePersist
    protected void onCreate()
    {
        modifiedOn = createdOn = Instant.now();
    }

    @PreUpdate
    protected void onUpdate()
    {
        modifiedOn = Instant.now();
    }

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

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(Set<Attachment> attachments) {
        this.attachments = attachments;
    }
}
