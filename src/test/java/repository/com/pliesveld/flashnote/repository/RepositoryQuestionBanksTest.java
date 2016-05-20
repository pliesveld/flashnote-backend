package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.repository.specifications.QuestionBankSpecification;
import com.pliesveld.flashnote.spring.CustomRepositoryPopulatorFactoryBean;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import com.pliesveld.tests.AbstractRepositoryUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(classes = { SpringDataTestConfig.class }, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = { RepositoryQuestionBanksTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext

public class RepositoryQuestionBanksTest extends AbstractRepositoryUnitTest {

    @Bean(name = "populator")
    CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean()
    {
        CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean = new CustomRepositoryPopulatorFactoryBean();
        customRepositoryPopulatorFactoryBean.setResources(new Resource[]{ new ClassPathResource("test-data-question-bank.json", this.getClass()) });
        return customRepositoryPopulatorFactoryBean;
    }

    @Test
    public void testLoadRepositoryFromJson()
    {
        assertTrue(questionRepository.count() > 0);
        assertTrue(categoryRepository.count() > 0);
        assertTrue(questionBankRepository.count() > 0);

    }

    @Test
    public void testFindQuestionBank()
    {
        LOG_SQL.info("Categories");
        categoryRepository.findAll().forEach(LOG_SQL::info);

        LOG_SQL.info("QuestionBank");
        questionBankRepository.findAll().forEach(LOG_SQL::info);

        LOG_SQL.info("Questions");
        questionRepository.findAll().forEach(LOG_SQL::info);
    }


    @Test
    @Transactional
    public void testQuestionBankSpec() {
        enableSQL();
        Specification<QuestionBank> spec = QuestionBankSpecification.descriptionContainsIgnoreCase("FINDME");
        List<QuestionBank> qb = questionBankRepository.findAll(spec);

        LOG_SQL.debug("QuestionBanks returned: {}", qb.size());

        qb.forEach(LOG_SQL::debug);
        qb.forEach((bank) -> {debug(bank); });

    }

}



