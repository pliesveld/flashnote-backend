package com.pliesveld.flashnote.domain;

import org.junit.After;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;

@Component
public class AbstractTransactionalDomainEntityUnitTest extends AbstractDomainEntityUnitTest
{
    @PersistenceContext
    protected EntityManager entityManager;

    @After
    final public void givenFlushAfterUnitTest()
    {
        if(entityManager != null && entityManager.isJoinedToTransaction()) {
            LOG.error("Flushing Persistence Context");
            entityManager.flush();
        }
        disableSQL();
    }

    @BeforeTransaction
    public void beforeTransaction() {
        LOG.error("Before Transaction");
    }

    @AfterTransaction
    public void afterTransaction() {
        LOG.error("After Transaction");
    }

    void logSessionFlush(EntityManager entityManager)
    {
        MySessionFlushEndListener listener = (entities, collections) -> { LOG.info("Flush ended with #{} entities and col #{} collections", entities, collections); };
        entityManager.unwrap(org.hibernate.Session.class).addEventListeners(listener);
    }


    public static enum EntityState {
        TRANSIENT,
        PERSISTENT,
        DETACHED;
    }

    protected void assertEntityHasState(Object entity, EntityState expected) {
        EntityState actual = stateOf(entity);
        assertEquals(expected,actual);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    protected EntityState stateOf(Object object) {
        if(entityManager.contains(object)) {
            return EntityState.PERSISTENT;
        }
        entityManager.flush();
        Serializable id = (Serializable) entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(object);
        if(id == null) {
            return EntityState.TRANSIENT;
        }
        return EntityState.DETACHED;
    }


}


