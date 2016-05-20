package com.pliesveld.tests;

import com.pliesveld.tests.listeners.LogHibernateTestExecutionListener;
import org.junit.After;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestExecutionListeners;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
@TestExecutionListeners(listeners = LogHibernateTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class AbstractTransactionalRepositoryUnitTest extends AbstractRepositoryUnitTest
{
    @PersistenceContext
    protected EntityManager entityManager;

    @After
    public void flushAfterTest()
    {
        if(entityManager != null && entityManager.isJoinedToTransaction())
        {
            LOG_SQL.debug("Flushing Persistence Context");
            entityManager.flush();
        }
    }


}
