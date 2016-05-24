package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy(value = {
    @ContextConfiguration(classes = { SpringDataTestConfig.class }, loader = AnnotationConfigContextLoader.class),
    @ContextConfiguration(classes = { RepositoryCategoriesTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
@Configuration
public class RepositoryCategoriesTest extends AbstractPopulatedRepositoryUnitTest {
    private static final Logger LOG = LogManager.getLogger();

    @Override
    protected Resource[] repositoryProperties() {
        return new Resource[]{ new ClassPathResource("test-data-category.json", this.getClass()) };
    }

    @Test
    @Transactional
    public void testLoadRepositoryFromJson()
    {
        assertTrue(categoryRepository.count() > 0);
    }
}