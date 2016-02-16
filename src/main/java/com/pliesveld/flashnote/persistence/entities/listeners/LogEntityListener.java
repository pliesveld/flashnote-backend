package com.pliesveld.flashnote.persistence.entities.listeners;

import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogEntityListener {
    static final Logger LOG = LoggerFactory.getLogger(LogEntityListener.class);
    
    @PrePersist
    private void logPrePersist(Object object)
    {
        LOG.debug("Persisting " + object);
    }

    @PostPersist
    private void logPostPersist(Object object)
    {
        LOG.debug("Persisted " + object);
    }
    
    @PostLoad
    private void logLoad(Object object)
    {
        LOG.debug("Loading " + object);
    }
    
    @PreUpdate
    private void logPreUpdate(Object object)
    {
        LOG.debug("Updating " + object);
    }
    
    @PostUpdate
    private void logPostUpdate(Object object)
    {
        LOG.debug("Updated " + object);
    }
    
    @PreRemove
    private void logPreRemove(Object object)
    {
        LOG.debug("Removing " + object);
    }

    @PostRemove
    private void logPostRemove(Object object)
    {
        LOG.debug("Removed " + object);
    }


    

}
