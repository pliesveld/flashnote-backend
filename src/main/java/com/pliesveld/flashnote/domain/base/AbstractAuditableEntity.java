package com.pliesveld.flashnote.domain.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pliesveld.flashnote.domain.converter.InstantConverter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

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
public abstract class AbstractAuditableEntity<ID extends Serializable> extends DomainBaseEntity<ID> {

    @Column(name = "CREATED")
    @Convert(converter = InstantConverter.class)
    @CreatedDate
    @JsonProperty(value = "created", access = READ_ONLY)
    protected Instant createdOn;

    @Column(name = "MODIFIED")
    @Convert(converter = InstantConverter.class)
    @LastModifiedDate
    @JsonProperty(value = "modified", access = READ_ONLY)
    protected Instant modifiedOn;

    @Column(name = "CREATED_BY")
    @CreatedBy
    @JsonProperty(value = "created_by", access = READ_ONLY)
    protected String createdByUser;

    @Column(name = "MODIFIED_BY")
    @LastModifiedBy
    @JsonProperty(value = "modified_by", access = READ_ONLY)
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

    public Instant getModifiedOn() { return modifiedOn; }

    public String getCreatedByUser() {
        return createdByUser;
    }

    public String getModifiedByUser() {
        return modifiedByUser;
    }
}
