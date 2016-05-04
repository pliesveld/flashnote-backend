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

    protected Instant createdOn;
    protected Instant modifiedOn;

    protected String createdByUser;
    protected String modifiedByUser;

    @Column(name = "CREATED")
    @Convert(converter = InstantConverter.class)
    @CreatedDate
    @JsonProperty(value = "created", access = READ_ONLY)
    public Instant getCreatedOn() {
        return createdOn;
    }

    @Column(name = "MODIFIED")
    @Convert(converter = InstantConverter.class)
    @LastModifiedDate
    @JsonProperty(value = "modified", access = READ_ONLY)
    public Instant getModifiedOn() { return modifiedOn; }

    @Column(name = "CREATED_BY")
    @CreatedBy
    @JsonProperty(value = "created_by", access = READ_ONLY)
    public String getCreatedByUser() {
        return createdByUser;
    }

    @Column(name = "MODIFIED_BY")
    @LastModifiedBy
    @JsonProperty(value = "modified_by", access = READ_ONLY)
    public String getModifiedByUser() {
        return modifiedByUser;
    }


    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public void setModifiedOn(Instant modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public void setCreatedByUser(String createdByUser) {
        this.createdByUser = createdByUser;
    }

    public void setModifiedByUser(String modifiedByUser) {
        this.modifiedByUser = modifiedByUser;
    }

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

}
