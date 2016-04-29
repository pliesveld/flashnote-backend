package com.pliesveld.flashnote.persistence.entities.listeners;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;

import static com.pliesveld.flashnote.logging.Markers.*;

public class LogEntityListener {
    private static final Logger LOG = LogManager.getLogger();

    @PrePersist
    private void logPrePersist(Object object)
    {
        if( LOG.isTraceEnabled() && LOG.isTraceEnabled(SQL_PERSIST))
            LOG.trace(SQL_PERSIST, "Persisting " + object);
    }

    @PostPersist
    private void logPostPersist(Object object)
    {
        if( LOG.isTraceEnabled() && LOG.isTraceEnabled(SQL_PERSIST))
            LOG.trace(SQL_PERSIST, "Persisted " + object);
    }
    
    @PostLoad
    private void logLoad(Object object)
    {
        if( LOG.isTraceEnabled() && LOG.isTraceEnabled(SQL_QUERY))
            LOG.trace(SQL_QUERY, "Loading " + object);
    }
    
    @PreUpdate
    private void logPreUpdate(Object object)
    {
        if( LOG.isTraceEnabled() && LOG.isTraceEnabled(SQL_UPDATE))
            LOG.trace(SQL_UPDATE, "Updating " + object);
    }
    
    @PostUpdate
    private void logPostUpdate(Object object)
    {
        if( LOG.isTraceEnabled() && LOG.isTraceEnabled(SQL_UPDATE))
            LOG.trace(SQL_UPDATE, "Updated " + object);
    }
    
    @PreRemove
    private void logPreRemove(Object object)
    {
        if( LOG.isTraceEnabled() && LOG.isTraceEnabled(SQL_DELETE))
            LOG.trace(SQL_DELETE, "Removing " + object);
    }

    @PostRemove
    private void logPostRemove(Object object)
    {
        if( LOG.isTraceEnabled() && LOG.isTraceEnabled(SQL_DELETE))
            LOG.trace(SQL_DELETE, "Removed " + object);
    }
}
