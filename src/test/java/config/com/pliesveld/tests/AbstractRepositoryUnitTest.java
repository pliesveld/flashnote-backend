package com.pliesveld.tests;

import com.pliesveld.flashnote.repository.*;
import com.pliesveld.tests.listeners.LogHibernateTestExecutionListener;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Component
@TestExecutionListeners(listeners = LogHibernateTestExecutionListener.class, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class AbstractRepositoryUnitTest
{
    protected static final Logger LOG_SQL = LogManager.getLogger("org.hibernate.SQL");

    private final static String LOG_TAG = "LOG_SQL_LEVEL";

    protected static void disableSQL()
    {
        System.setProperty(LOG_TAG, "ERROR");
        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
    }

    protected static void enableSQL()
    {
        System.setProperty(LOG_TAG, "DEBUG");
        ((org.apache.logging.log4j.core.LoggerContext) LogManager.getContext(false)).reconfigure();
    }


    @Autowired(required = false)
    protected AnswerRepository answerRepository;

    @Autowired(required = false)
    protected AttachmentBinaryRepository attachmentBinaryRepository;

    @Autowired(required = false)
    protected AttachmentRepository attachmentRepository;

    @Autowired(required = false)
    protected AttachmentTextRepository attachmentTextRepository;

    @Autowired(required = false)
    protected CategoryRepository categoryRepository;

    @Autowired(required = false)
    protected DeckRepository deckRepository;

    @Autowired(required = false)
    protected FlashCardRepository flashCardRepository;

    @Autowired(required = false)
    protected QuestionRepository questionRepository;

    @Autowired(required = false)
    protected QuestionBankRepository questionBankRepository;

    @Autowired(required = false)
    protected StatementRepository statementRepository;

    @Autowired(required = false)
    protected StudentDetailsRepository studentDetailsRepository;

    @Autowired(required = false)
    protected StudentRepository studentRepository;

    @Autowired(required = false)
    protected PasswordResetRepository passwordResetRepository;

    @Autowired(required = false)
    protected RegistrationRepository registrationRepository;

    @Autowired(required = false)
    protected NotificationRepository notificationRepository;



    @Transactional
    public void testRepositoryLoads()
    {
        assertNotNull(answerRepository);
        assertNotNull(attachmentBinaryRepository);
        assertNotNull(attachmentRepository);
        assertNotNull(attachmentTextRepository);
        assertNotNull(categoryRepository);
        assertNotNull(deckRepository);
        assertNotNull(flashCardRepository);
        assertNotNull(questionRepository);
        assertNotNull(questionBankRepository);
        assertNotNull(statementRepository);
        assertNotNull(studentDetailsRepository);
        assertNotNull(studentRepository);
        assertNotNull(registrationRepository);
        assertNotNull(passwordResetRepository);
        assertNotNull(notificationRepository);

    }

    protected void assertAnswerRepositoryCount(long value)
    {
            assertEquals("The entity count of AnswerRepository should be " + value, value, answerRepository.count());
    }
    protected void assertAttachmentBinaryRepositoryCount(long value)
    {
            assertEquals("The entity count of AttachmentBinaryRepository should be " + value, value, attachmentBinaryRepository.count());
    }
    protected void assertAttachmentRepositoryCount(long value)
    {
            assertEquals("The entity count of AttachmentRepository should be " + value, value, attachmentBinaryRepository.count() + attachmentTextRepository.count());
    }
    protected void assertAttachmentTextRepositoryCount(long value)
    {
            assertEquals("The entity count of AttachmentTextRepository should be " + value, value, attachmentTextRepository.count());
    }
    protected void assertCategoryRepositoryCount(long value)
    {
            assertEquals("The entity count of CategoryRepository should be " + value, value, categoryRepository.count());
    }
    protected void assertDeckRepositoryCount(long value)
    {
            assertEquals("The entity count of DeckRepository should be " + value, value, deckRepository.count());
    }
    protected void assertFlashCardRepositoryCount(long value)
    {
            assertEquals("The entity count of FlashCardRepository should be " + value, value, flashCardRepository.count());
    }
    protected void assertQuestionRepositoryCount(long value)
    {
            assertEquals("The entity count of QuestionRepository should be " + value, value, questionRepository.count());
    }
    protected void assertQuestionBankRepositoryCount(long value)
    {
        assertEquals("The entity count of QuestionRepository should be " + value, value, questionBankRepository.count());
    }
    protected void assertStatementRepositoryCount(long value)
    {
            assertEquals("The entity count of StatementRepository should be " + value, value, questionRepository.count() + answerRepository.count());
    }
    protected void assertStudentDetailsRepositoryCount(long value)
    {
            assertEquals("The entity count of StudentDetailsRepository should be " + value, value, studentDetailsRepository.count());
    }
    protected void assertStudentRepositoryCount(long value)
    {
            assertEquals("The entity count of StudentRepository should be " + value, value, studentRepository.count());
    }
    protected void assertRegistrationRepositoryCount(long value)
    {
        assertEquals("The entity count of RegistrationRepository should be " + value, value, registrationRepository.count());
    }
    protected void assertPasswordResetRepositoryCount(long value)
    {
        assertEquals("The entity count of PasswordResetRepository should be " + value, value, passwordResetRepository.count());
    }
    protected void assertNotificationRepositoryCount(long value) {
        assertEquals("The entity count of NotificationRepository should be " + value, value, notificationRepository.count());
    }

    protected static void debug(Object obj) {
        //  Will cause problems if used on hibernate proxy objects, and on circular references.
        //  Use with care.
        LOG_SQL.debug(ReflectionToStringBuilder.toString(obj));
    }
}
