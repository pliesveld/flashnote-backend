package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Question;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(classes = { SpringDataTestConfig.class }, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = { RepositoryQuestionsTest.class }, loader = AnnotationConfigContextLoader.class)
})
public class RepositoryQuestionsTest extends AbstractTransactionalRepositoryUnitTest {
    private static final Logger LOG = LogManager.getLogger();

    @Bean(name = "populator")
    CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean()
    {
        CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean = new CustomRepositoryPopulatorFactoryBean();
        customRepositoryPopulatorFactoryBean.setResources(new Resource[]{ new ClassPathResource("test-data-questions.json", this.getClass()) });
        return customRepositoryPopulatorFactoryBean;
    }

    @Test
    public void testLoadRepositoryFromJson()
    {
        assertTrue(questionRepository.count() > 0);
        assertEquals(questionRepository.count(), 3);
    }

    @Test
    @Transactional
    public void givenQuestion_whenChangingTitle_thenPersisted()
    {
        Question question = questionRepository.findOne(1);
        debug(question);
        question.setTitle("Change title");
    }
}



