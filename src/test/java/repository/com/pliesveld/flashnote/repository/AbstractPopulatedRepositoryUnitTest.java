package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.populator.spring.RepositoryPopulatorConfig;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import com.pliesveld.tests.AbstractTransactionalRepositoryUnitTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.Serializable;

@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy(value = {
        @ContextConfiguration(classes = { SpringDataTestConfig.class }, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(name = "REPOSITORY", classes = { DefaultRepositorySettingsConfig.class }, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = { RepositoryPopulatorConfig.class }, loader = AnnotationConfigContextLoader.class)

})
public abstract class AbstractPopulatedRepositoryUnitTest extends AbstractTransactionalRepositoryUnitTest {
    private static final Logger LOG = LogManager.getLogger();


    protected <T extends DomainBaseEntity<ID>, ID extends Serializable> void debugRepository(CrudRepository<T,ID> repository) {
//        Asserts.check(entityManager.isJoinedToTransaction(), "Transaction Required");
        LOG_SQL.info("Repository Count = {}", repository.count());
        repository.findAll().forEach(this::debugEntity);
    }

    protected void debugRepositoryCount() {
        long que_count = questionRepository.count();
        long ans_count = answerRepository.count();
        long fc_count = flashCardRepository.count();
        long cat_count = categoryRepository.count();
        long deck_count = deckRepository.count();
        long bank_count = questionBankRepository.count();

        LOG_SQL.info("Question count: {}", que_count);
        LOG_SQL.info("Answer count: {}", ans_count);
        LOG_SQL.info("FlashCard count: {}", fc_count);
        LOG_SQL.info("Category count: {}", cat_count);
        LOG_SQL.info("Deck count: {}", deck_count);
        LOG_SQL.info("QuestionBank count: {}", bank_count);
    }


    protected void debugRepository() {
        LOG_SQL.info("Displaying Repositories:");

        LOG_SQL.info("Categories");
        categoryRepository.findAll().forEach(this::debugEntity);
        entityManager.clear();

        LOG_SQL.info("Questions");
        questionRepository.findAll().forEach(this::debugEntity);
        entityManager.clear();

        LOG_SQL.info("Bank");
        questionBankRepository.findAll().forEach(this::debugEntity);
        entityManager.clear();

        LOG_SQL.info("Answers");
        answerRepository.findAll().forEach(this::debugEntity);
        entityManager.clear();

        LOG_SQL.info("Flashcards");
        flashCardRepository.findAll().forEach(this::debugEntity);
        entityManager.clear();

        LOG_SQL.info("Deck");
        deckRepository.findAll().forEach(this::debugEntity);
        entityManager.clear();

    }

}