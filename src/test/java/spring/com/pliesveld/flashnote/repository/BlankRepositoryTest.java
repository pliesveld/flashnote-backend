package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.spring.CustomRepositoryPopulatorFactoryBean;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(classes = { SpringDataTestConfig.class }, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = { BlankRepositoryTest.class }, loader = AnnotationConfigContextLoader.class)
})
public class BlankRepositoryTest {
    private static final Logger LOG = LogManager.getLogger();

    @Bean(name = "populator")
    CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean()
    {
        CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean = new CustomRepositoryPopulatorFactoryBean();
        customRepositoryPopulatorFactoryBean.setResources(new Resource[]{ new ClassPathResource("test-data-blank.json", this.getClass()) });
        return customRepositoryPopulatorFactoryBean;
    }

    @Autowired
    CategoryRepository categoryRepository;


    @Test
    public void testContext()
    {
        assertNotNull(categoryRepository);
    }
}