package com.pliesveld.flashnote.domain.base;

import com.pliesveld.flashnote.domain.converter.InstantConverter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * Entities that inherit from this @MappedSuperclass-annotated class
 * will have have timestamps for when they were created, and when
 * they were last modified.
 *
 * An Auditing Entity Listener is attached for notification when this entity is created or modified.  The Spring-Data
 * auditing annotations @CreatedDate / @LastModifiedDate / @CreatedBy / @LastModifiedBy update the fields they mark
 * with auditing information.
 *
 * @see com.pliesveld.flashnote.security.UsernameAuditorAware
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditableEntity<U,PK extends Serializable> {

    @Column(name = "CREATED")
    @Convert(converter = InstantConverter.class)
    @CreatedDate
    protected Instant createdOn;

    @Column(name = "MODIFIED")
    @Convert(converter = InstantConverter.class)
    @LastModifiedDate
    protected Instant modifiedOn;

    @Column(name = "CREATED_BY")
    @CreatedBy
    protected String createdByUser;

    @Column(name = "MODIFIED_BY")
    @LastModifiedBy
    protected String modifiedByUser;

    @PrePersist
    protected void onCreate()
    {
        modifiedOn = createdOn = Instant.now();
        if(createdByUser == null)
        {
            createdByUser = "SYSTEM";
        }
        if(modifiedByUser == null)
        {
            modifiedByUser = createdByUser;
        }
    }

    @PreUpdate
    protected void onUpdate()
    {
        modifiedOn = Instant.now();
        if(modifiedByUser == null)
            modifiedByUser = "SYSTEM";
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
