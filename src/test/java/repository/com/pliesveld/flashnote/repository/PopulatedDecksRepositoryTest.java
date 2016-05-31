package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.repository.RepositorySettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.annotation.Bean;
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(name = "REPOSITORY", classes = { PopulatedDecksRepositoryTest.class }, loader = AnnotationConfigContextLoader.class)
})
@DirtiesContext
public class PopulatedDecksRepositoryTest extends AbstractPopulatedRepositoryUnitTest {

    @Bean
    public RepositorySettings repositorySettings() {
        RepositorySettings repositorySettings = new RepositorySettings(new Resource[] {new ClassPathResource("test-data-deck.json", this.getClass()) });
        return repositorySettings;
    }

    @Test
    public void whenContextLoad_thenCorrect() {

    }

    public void testRepositoryCount() {
        long que_count = questionRepository.count();
        long ans_count = answerRepository.count();
        long cat_count = categoryRepository.count();
        long deck_count = deckRepository.count();
        long bank_count = questionBankRepository.count();

        LOG_SQL.info("Question count: {}", que_count);
        LOG_SQL.info("Answer count: {}", ans_count);
        LOG_SQL.info("Category count: {}", cat_count);
        LOG_SQL.info("Deck count: {}", deck_count);
        LOG_SQL.info("QuestionBank count: {}", bank_count);

        assertTrue(que_count > 0);
        assertTrue(ans_count > 0);
        assertTrue(cat_count > 0);
        assertTrue(deck_count > 0);

    }

    @Transactional
    public void testFindDecks()
    {
        debugRepository();
    }


//    @Test
//    @Transactional
//    @DirtiesContext
//    public void testDeckSpecTest() {
//        Specification<Deck> spec = DeckSpecification.descriptionOrFlashcardContainsIgnoreCase("FINDME");
//        List<Deck> decks = deckRepository.findAll(spec);
//
//
//        LOG_SQL.debug("Decks returned: {}", decks.size());
//
//        decks.forEach(LOG_SQL::debug);
//        decks.forEach((bank) -> {
//            debug(bank);
//        });
//
//    }


}

