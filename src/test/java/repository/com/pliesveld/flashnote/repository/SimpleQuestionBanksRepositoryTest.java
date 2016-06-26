package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.repository.specifications.QuestionBankSpecification;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.populator.repository.reader.RepositorySettings;
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
        @ContextConfiguration(name = "REPOSITORY", classes = { SimpleQuestionBanksRepositoryTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class SimpleQuestionBanksRepositoryTest extends AbstractPopulatedRepositoryUnitTest {


    @Bean
    public RepositorySettings repositorySettings() {
        RepositorySettings repositorySettings = new RepositorySettings(new Resource[] {new ClassPathResource("test-data-question-bank.json", this.getClass()) });
        return repositorySettings;
    }

    @Test
    public void whenContextLoad_thenCorrect()
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

    public void testQuestionBankSpec() {
        Specification<QuestionBank> spec = QuestionBankSpecification.descriptionContainsIgnoreCase("");
        List<QuestionBank> qb = questionBankRepository.findAll(spec);

        LOG_SQL.debug("QuestionBanks returned: {}", qb.size());

        qb.forEach(LOG_SQL::debug);
        qb.forEach((bank) -> {debug(bank); });

    }

}



