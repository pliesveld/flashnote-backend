package com.pliesveld.flashnote.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.Instant;

/**
 * Entities that inherit from this @MappedSuperclass-annotated class
 * will have have timestamps for when they were created, and when
 * they were last modified
 */
@MappedSuperclass
public abstract class AbstractDatedEntity {
    @Column(name = "CREATED")
    protected Instant createdOn;

    @Column(name = "MODIFIED")
    protected Instant modifiedOn;

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
}
