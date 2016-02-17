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
