package com.pliesveld.flashnote.persistence.entities.listeners;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class LogEntityListener {
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();
    
    @PrePersist
    private void logPrePersist(Object object)
    {
        LOG.trace("Persisting " + object);
    }

    @PostPersist
    private void logPostPersist(Object object)
    {
        LOG.trace("Persisted " + object);
    }
    
    @PostLoad
    private void logLoad(Object object)
    {
        LOG.trace("Loading " + object);
    }
    
    @PreUpdate
    private void logPreUpdate(Object object)
    {
        LOG.trace("Updating " + object);
    }
    
    @PostUpdate
    private void logPostUpdate(Object object)
    {
        LOG.trace("Updated " + object);
    }
    
    @PreRemove
    private void logPreRemove(Object object)
    {
        LOG.trace("Removing " + object);
    }

    @PostRemove
    private void logPostRemove(Object object)
    {
        LOG.trace("Removed " + object);
    }
}
