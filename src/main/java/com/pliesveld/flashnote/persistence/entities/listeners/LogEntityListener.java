package com.pliesveld.flashnote.persistence.entities.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.persistence.*;

import static com.pliesveld.flashnote.logging.Markers.*;

@Component
public class LogEntityListener {
    private static final Logger LOG = LogManager.getLogger("javax.persistence.Entity");

    @PrePersist
    private void logPrePersist(Object object)
    {
        if( LOG.isDebugEnabled() && LOG.isDebugEnabled(SQL_PERSIST))
            LOG.debug(SQL_PERSIST, "Persisting " + object);
    }

    @PostPersist
    private void logPostPersist(Object object)
    {
        if( LOG.isDebugEnabled() && LOG.isDebugEnabled(SQL_PERSIST))
            LOG.debug(SQL_PERSIST, "Persisted " + object);
    }
    
    @PostLoad
    private void logLoad(Object object)
    {
        if( LOG.isDebugEnabled() && LOG.isDebugEnabled(SQL_QUERY))
            LOG.debug(SQL_QUERY, "Load " + object);
    }
    
    @PreUpdate
    private void logPreUpdate(Object object)
    {
        if( LOG.isDebugEnabled() && LOG.isDebugEnabled(SQL_UPDATE))
            LOG.debug(SQL_UPDATE, "Updating " + object);
    }
    
    @PostUpdate
    private void logPostUpdate(Object object)
    {
        if( LOG.isDebugEnabled() && LOG.isDebugEnabled(SQL_UPDATE))
            LOG.debug(SQL_UPDATE, "Updated " + object);
    }
    
    @PreRemove
    private void logPreRemove(Object object)
    {
        if( LOG.isDebugEnabled() && LOG.isDebugEnabled(SQL_DELETE))
            LOG.debug(SQL_DELETE, "Removing " + object);
    }

    @PostRemove
    private void logPostRemove(Object object)
    {
        if( LOG.isDebugEnabled() && LOG.isDebugEnabled(SQL_DELETE))
            LOG.debug(SQL_DELETE, "Removed " + object);
    }
}
