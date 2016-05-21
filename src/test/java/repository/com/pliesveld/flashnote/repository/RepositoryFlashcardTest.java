package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.spring.CustomRepositoryFactoryBeanSettings;
import com.pliesveld.flashnote.spring.CustomRepositoryPopulatorFactoryBean;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import com.pliesveld.tests.AbstractTransactionalRepositoryUnitTest;
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

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(classes = { SpringDataTestConfig.class }, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = { RepositoryFlashcardTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class RepositoryFlashcardTest extends AbstractTransactionalRepositoryUnitTest {
    private static final Logger LOG = LogManager.getLogger();

    @Bean(name = "populator")
    CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean(CustomRepositoryFactoryBeanSettings customRepositoryFactoryBeanSettings)
    {
        CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean = new CustomRepositoryPopulatorFactoryBean(customRepositoryFactoryBeanSettings);
        customRepositoryPopulatorFactoryBean.setResources(new Resource[]{ new ClassPathResource("test-data-flashcards.json", this.getClass()) });
        return customRepositoryPopulatorFactoryBean;
    }

    @Test
    @DirtiesContext
    public void testLoadRepositoryFromJson()
    {
        assertEquals(1, questionRepository.count());
        assertEquals(1, answerRepository.count());
        assertEquals(1, flashCardRepository.count());
    }

    @Test
    public void whenPrintContents()
    {
        flashCardRepository.findAll().forEach(AbstractTransactionalRepositoryUnitTest::debug);
    }
}



