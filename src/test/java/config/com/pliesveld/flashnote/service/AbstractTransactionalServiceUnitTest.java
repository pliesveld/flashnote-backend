package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.repository.AbstractPopulatedRepositoryUnitTest;
import com.pliesveld.flashnote.repository.DefaultRepositorySettingsConfig;
import com.pliesveld.flashnote.repository.PopulatedCategoriesRepositoryTest;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import com.pliesveld.flashnote.spring.SpringServiceTestConfig;
import com.pliesveld.flashnote.spring.repository.RepositoryPopulatorConfig;
import com.pliesveld.tests.AbstractRepositoryUnitTest;
import com.pliesveld.tests.AbstractTransactionalRepositoryUnitTest;
import org.junit.After;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
    @ContextConfiguration(classes = { SpringServiceTestConfig.class }, loader = AnnotationConfigContextLoader.class),
    @ContextConfiguration(classes = { SpringDataTestConfig.class }, loader = AnnotationConfigContextLoader.class),
    @ContextConfiguration(name = "REPOSITORY", classes = { DefaultRepositorySettingsConfig.class }, loader = AnnotationConfigContextLoader.class),
    @ContextConfiguration(classes = { RepositoryPopulatorConfig.class }, loader = AnnotationConfigContextLoader.class)
})
public class AbstractTransactionalServiceUnitTest extends AbstractRepositoryUnitTest
{
    @PersistenceContext
    protected EntityManager entityManager;

    @After
    public void flushAfter()
    {
        if(entityManager != null && entityManager.isJoinedToTransaction())
        {
            LOG_SQL.debug("Flushing Persistence Context");
            entityManager.flush();
        }
    }


}
