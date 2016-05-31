package com.pliesveld.tests;

import com.pliesveld.tests.listeners.LogHibernateTestExecutionListener;
import org.junit.After;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestExecutionListeners;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public abstract class AbstractTransactionalRepositoryUnitTest extends AbstractRepositoryUnitTest
{
    @PersistenceContext
    protected EntityManager entityManager;

    @After
    public void flushAfter()
    {
        if(entityManager != null && entityManager.isJoinedToTransaction())
        {
            LOG_SQL.trace("Flushing Persistence Context");
            entityManager.flush();
        }
    }


}
