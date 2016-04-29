package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.repository.*;
import com.pliesveld.flashnote.util.generator.QuestionGenerator;
import com.pliesveld.flashnote.util.generator.StudentGenerator;
import org.apache.commons.lang3.RandomUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Component
public class AbstractDomainEntityUnitTest
{
    protected static final Logger LOG = LogManager.getLogger("BaseEntityTests");

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



    @Bean
    @Scope("prototype")
    public Question questionBean()
    {
        return QuestionGenerator.randomQuestion();
    }

    @Bean
    @Scope("prototype")
    public QuestionBank questionBankBean()
    {
        QuestionBank questionBank = new QuestionBank();
        questionBank.setCategory(this.categoryBean());
        questionBank.setDescription(UUID.randomUUID().toString());
        return questionBank;
    }

    @Bean
    @Scope("prototype")
    public Answer answerBean()
    {
        Answer answer = new Answer();
        answer.setContent("The answer is " + UUID.randomUUID().toString());
        return answer;
    }

    @Bean
    @Scope("prototype")
    public AttachmentText attachmentTextBean()
    {
        AttachmentText attachmentText = new AttachmentText();
        String contents = StringUtils.arrayToCommaDelimitedString(new Object[]{UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()});
        attachmentText.setContents(contents);
        attachmentText.setFileName(UUID.randomUUID().toString().concat(".txt"));
        attachmentText.setAttachmentType(AttachmentType.TEXT);
        return attachmentText;
    }

    @Bean
    @Scope("prototype")
    public AttachmentBinary attachmentBinaryBean()
    {
        AttachmentBinary attachmentBinary = new AttachmentBinary();
        byte[] contents = RandomUtils.nextBytes(1024);
        attachmentBinary.setContents(contents);
        attachmentBinary.setFileName(UUID.randomUUID().toString().concat(".txt"));
        return attachmentBinary;
    }
    @Bean
    @Scope("prototype")
    public Category categoryBean()
    {
        Category category = new Category();
        category.setName(UUID.randomUUID().toString().substring(0,5));
        category.setDescription(UUID.randomUUID().toString());
        return category;
    }

    private static int count = 0;

    public static synchronized int incrementCounter() {
        count++;
        return count;
    }

    @Bean
    @Scope("prototype")
    public Deck deckBean()
    {
        Deck deck = new Deck();
        StudentDetails studentDetails = this.studentDetailsAndStudentBean();
        deck.setAuthor(studentDetails);
        deck.setDescription("Deck title #" + incrementCounter());
        deck.setCategory(this.categoryBean());

        List<FlashCard> list = new ArrayList<FlashCard>();
        int max = RandomUtils.nextInt(0,5);
        for(int i = 0; i < max ; i ++)
        {
            list.add(this.flashcardBean());
        }
        deck.setFlashcards(list);

        return deck;
    }

    @Bean
    @Scope("prototype")
    public FlashCard flashcardBean()
    {
        Question question = this.questionBean();
        Answer answer = this.answerBean();
        FlashCard flashCard = new FlashCard(question,answer);
        return flashCard;
    }

    @Bean
    @Scope("prototype")
    public Student studentBean()
    {
        return StudentGenerator.randomizedStudent(false);
    }

    @Bean
    @Scope("prototype")
    public StudentDetails studentDetailsBean()
    {
        return StudentGenerator.randomizedStudentDetails(false);
    }

    @Bean
    @Scope("prototype")
    public Student studentAndStudentDetailsBean()
    {
        return StudentGenerator.randomizedStudent(true);
    }

    @Bean
    @Scope("prototype")
    public StudentDetails studentDetailsAndStudentBean()
    {
        return StudentGenerator.randomizedStudentDetails(true);
    }

    @Bean
    @Scope("prototype")
    public AccountPasswordResetToken accountPasswordResetTokenBean(Student student)
    {
        String token = UUID.randomUUID().toString().toUpperCase();
        AccountPasswordResetToken pw_token = new AccountPasswordResetToken(student, token);
        return pw_token;
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
