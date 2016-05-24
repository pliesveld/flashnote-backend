package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.repository.specifications.QuestionBankSpecification;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import com.pliesveld.tests.AbstractTransactionalRepositoryUnitTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
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
@Configuration
public class RepositoryQuestionBanksTest extends AbstractPopulatedRepositoryUnitTest {

    @Override
    protected Resource[] repositoryProperties() {
        return new Resource[]{ new ClassPathResource("test-data-question-bank.json", this.getClass()) };
    }

    @Test
    @Transactional
    @DirtiesContext

    public void testLoadRepositoryFromJson()
    {
        long que_count = questionRepository.count();
        long cat_count = categoryRepository.count();
        long bank_count = questionBankRepository.count();

        LOG_SQL.info("Question count: {}", que_count);
        LOG_SQL.info("Category count: {}", cat_count);
        LOG_SQL.info("QuestionBank count: {}", bank_count);

        assertTrue(que_count > 0);
        assertTrue(cat_count > 0);
        assertTrue(bank_count > 0);

    }

    @Test
    @Transactional
    @DirtiesContext

    public void testFindQuestionBank()
    {
        LOG_SQL.info("Listing All Categories");
        categoryRepository.findAll().forEach(AbstractTransactionalRepositoryUnitTest::debug);

        LOG_SQL.info("Listing All QuestionBanks");
        questionBankRepository.findAll().forEach(AbstractTransactionalRepositoryUnitTest::debug);

        LOG_SQL.info("Listing All Questions");
        questionRepository.findAll().forEach(AbstractTransactionalRepositoryUnitTest::debug);
    }


    @Test
    @Transactional
    @DirtiesContext

    public void testQuestionBankSpec() {
        enableSQL();
        Specification<QuestionBank> spec = QuestionBankSpecification.descriptionContainsIgnoreCase("");
        List<QuestionBank> qb = questionBankRepository.findAll(spec);

        LOG_SQL.debug("QuestionBanks returned: {}", qb.size());

        qb.forEach(LOG_SQL::debug);
        qb.forEach((bank) -> {debug(bank); });

    }

}



