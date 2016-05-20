package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.spring.CustomRepositoryPopulatorFactoryBean;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import com.pliesveld.tests.AbstractRepositoryUnitTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
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

public class RepositoryCategoriesTest extends AbstractRepositoryUnitTest {
    private static final Logger LOG = LogManager.getLogger();

    @Bean(name = "populator")
    CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean()
    {
        CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean = new CustomRepositoryPopulatorFactoryBean();
        customRepositoryPopulatorFactoryBean.setResources(new Resource[]{ new ClassPathResource("test-data-category.json", this.getClass()) });
        return customRepositoryPopulatorFactoryBean;
    }

    @Test
    @Transactional
    public void testLoadRepositoryFromJson()
    {
        assertTrue(categoryRepository.count() > 0);
    }
}