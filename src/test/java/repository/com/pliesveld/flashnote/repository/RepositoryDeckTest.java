package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.repository.specifications.DeckSpecification;
import com.pliesveld.flashnote.spring.CustomRepositoryPopulatorFactoryBean;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import com.pliesveld.tests.AbstractRepositoryUnitTest;
import com.pliesveld.tests.AbstractTransactionalRepositoryUnitTest;
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
        @ContextConfiguration(classes = { RepositoryDeckTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class RepositoryDeckTest extends AbstractTransactionalRepositoryUnitTest {

    @Bean(name = "populator")
    CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean()
    {
        CustomRepositoryPopulatorFactoryBean customRepositoryPopulatorFactoryBean = new CustomRepositoryPopulatorFactoryBean();
        customRepositoryPopulatorFactoryBean.setResources(new Resource[]{ new ClassPathResource("test-data-deck.json", this.getClass()) });
        return customRepositoryPopulatorFactoryBean;
    }

    @Test
    @DirtiesContext
    public void testLoadRepositoryFromJson()
    {
        assertTrue(questionRepository.count() > 0);
        assertTrue(categoryRepository.count() > 0);
        assertTrue(deckRepository.count() > 0);

    }

    @Test
    @Transactional
    @DirtiesContext
    public void testFindDecks()
    {
        LOG_SQL.info("Categories");
        categoryRepository.findAll().forEach(AbstractRepositoryUnitTest::debug);

        LOG_SQL.info("QuestionBank");
        deckRepository.findAll().forEach(AbstractRepositoryUnitTest::debug);

        LOG_SQL.info("Questions");
        questionRepository.findAll().forEach(AbstractRepositoryUnitTest::debug);

        LOG_SQL.info("Answers");
        answerRepository.findAll().forEach(AbstractRepositoryUnitTest::debug);
    }


    @Test
    @Transactional
    @DirtiesContext
    public void testDeckSpecTest() {
        enableSQL();
        Specification<Deck> spec = DeckSpecification.descriptionOrFlashcardContainsIgnoreCase("FINDME");
        List<Deck> decks = deckRepository.findAll(spec);


        LOG_SQL.debug("Decks returned: {}", decks.size());

        decks.forEach(LOG_SQL::debug);
        decks.forEach((bank) -> {
            debug(bank);
        });

    }

}



