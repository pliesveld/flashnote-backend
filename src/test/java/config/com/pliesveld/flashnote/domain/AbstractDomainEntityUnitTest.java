package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.repository.*;
import com.pliesveld.tests.beans.DomainEntities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionEventListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Component
@Configuration
@Import(DomainEntities.class)
public class AbstractDomainEntityUnitTest implements ApplicationContextAware
{
    protected static final Logger LOG = LogManager.getLogger("BaseEntityTests");
    private final static String LOG_TAG = "LOG_SQL_LEVEL";

    protected ApplicationContext ctx;

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

    public Question questionBean()
    {
        return ctx.getBean(Question.class);
    }

    public QuestionBank questionBankBean()
    {
        return ctx.getBean(QuestionBank.class);
    }
    public Answer answerBean()
    {
        return ctx.getBean(Answer.class);
    }
    public AttachmentText attachmentTextBean()
    {
        return ctx.getBean(AttachmentText.class);
    }
    public AttachmentBinary attachmentBinaryBean()
    {
        return ctx.getBean(AttachmentBinary.class);
    }
    public Category categoryBean()
    {
        return ctx.getBean(Category.class);
    }

    private static int count = 0;

    public static synchronized int incrementCounter() {
        count++;
        return count;
    }
    public Deck deckBean()
    {
        return ctx.getBean(Deck.class);
    }
    public FlashCard flashcardBean()
    {
        return ctx.getBean(FlashCard.class);
    }
    public Student studentBean()
    {
        return ctx.getBean(Student.class);
    }
    public StudentDetails studentDetailsBean()
    {
        return ctx.getBean(StudentDetails.class);
    }

    @Deprecated
    public Student studentAndStudentDetailsBean()
    {
        return (Student) ctx.getBean("studentAndStudentDetailsBean");
    }

    @Deprecated
    public StudentDetails studentDetailsAndStudentBean()
    {
        return studentDetailsBean();
//        return (StudentDetails) ctx.getBean("studentDetailsBean");

    }

    public AccountPasswordResetToken accountPasswordResetTokenBean(Student student)
    {
        return ctx.getBean(AccountPasswordResetToken.class);
    }
    /*
    @Before
    public void setup()
    {
        /*
         * ApplicationContext ctx = new
         * AnnotationConfigApplicationContext(SpringConfig.class);
         * LocalEntityManagerFactoryBean sfb = (LocalEntityManagerFactoryBean)
         * ctx.getBean("&entityManager"); entityManager =
         * sfb.getConfiguration().buildEntityManagerFactory();
         */
 //   }

    void logSessionFlush(EntityManager entityManager)
    {
        MySessionFlushEndListener listener = (entities, collections) -> { LOG.info("Flush ended with #{} entities and col #{} collections", entities, collections); };
        entityManager.unwrap(org.hibernate.Session.class).addEventListeners(listener);
    }

    @Override
    final public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}


@FunctionalInterface
interface MySessionFlushEndListener extends SessionEventListener
{
    @Override
    public abstract void flushEnd(int numberOfEntities, int numberOfCollections);

    @Override
    public default void transactionCompletion(boolean successful) {

    }

    @Override
    public default void jdbcConnectionAcquisitionStart() {

    }

    @Override
    public default void jdbcConnectionAcquisitionEnd() {

    }

    @Override
    public default void jdbcConnectionReleaseStart() {

    }

    @Override
    public default void jdbcConnectionReleaseEnd() {

    }

    @Override
    public default void jdbcPrepareStatementStart() {

    }

    @Override
    public default void jdbcPrepareStatementEnd() {

    }

    @Override
    public default void jdbcExecuteStatementStart() {

    }

    @Override
    public default void jdbcExecuteStatementEnd() {

    }

    @Override
    public default void jdbcExecuteBatchStart(){}

    @Override
    public default void jdbcExecuteBatchEnd(){}

    @Override
    public default void cachePutStart(){}

    @Override
    public default void cachePutEnd(){}

    @Override
    public default void cacheGetStart(){}

    @Override
    public default void cacheGetEnd(boolean hit){}

    @Override
    public default void flushStart(){}

    @Override
    public default void partialFlushStart(){}

    @Override
    public default void partialFlushEnd(int numberOfEntities, int numberOfCollections){}

    @Override
    public default void dirtyCalculationStart(){}

    @Override
    public default void dirtyCalculationEnd(boolean dirty){}

    @Override
    public default void end(){}
}
