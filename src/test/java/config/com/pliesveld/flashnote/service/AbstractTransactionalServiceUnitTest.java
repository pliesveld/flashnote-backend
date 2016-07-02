package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.repository.DefaultRepositorySettingsConfig;
import com.pliesveld.flashnote.spring.Profiles;
import com.pliesveld.flashnote.spring.SpringDataTestConfig;
import com.pliesveld.flashnote.spring.SpringServiceTestConfig;
import com.pliesveld.flashnote.spring.audit.SpringAuditConfig;
import com.pliesveld.flashnote.spring.data.SpringDataConfig;
import com.pliesveld.flashnote.spring.db.PersistenceContext;
import com.pliesveld.populator.spring.RepositoryPopulatorConfig;
import com.pliesveld.tests.AbstractRepositoryUnitTest;
import com.pliesveld.tests.listeners.LogHibernateTestExecutionListener;
import org.junit.After;
import org.springframework.boot.test.IntegrationTestPropertiesListener;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.persistence.EntityManager;

//@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles(Profiles.INTEGRATION_TEST)
@ContextHierarchy({
        @ContextConfiguration(classes = {SpringDataTestConfig.class}, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(classes = {SpringDataConfig.class, SpringAuditConfig.class, PersistenceContext.class, SpringServiceTestConfig.class}, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(name = "REPOSITORY", classes = {DefaultRepositorySettingsConfig.class}, loader = AnnotationConfigContextLoader.class),
        @ContextConfiguration(name = "REPOSITORY_POPULATOR", classes = {RepositoryPopulatorConfig.class}, loader = AnnotationConfigContextLoader.class)
})
@TestExecutionListeners(listeners = {IntegrationTestPropertiesListener.class,
        DirtiesContextBeforeModesTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class, SqlScriptsTestExecutionListener.class, LogHibernateTestExecutionListener.class})
public class AbstractTransactionalServiceUnitTest extends AbstractRepositoryUnitTest {
    @javax.persistence.PersistenceContext
    protected EntityManager entityManager;

    @After
    public void flushAfter() {
        if (entityManager != null && entityManager.isJoinedToTransaction()) {
            LOG_SQL.debug("Flushing Persistence Context");
            entityManager.flush();
        }
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

        LOG_SQL.info("Questions");
        questionRepository.findAll().forEach(this::debugEntity);

        LOG_SQL.info("Bank");
        questionBankRepository.findAll().forEach(this::debugEntity);

        LOG_SQL.info("Answers");
        answerRepository.findAll().forEach(this::debugEntity);

        LOG_SQL.info("Flashcards");
        flashCardRepository.findAll().forEach(this::debugEntity);

        LOG_SQL.info("Deck");
        deckRepository.findAll().forEach(this::debugEntity);

    }
}
