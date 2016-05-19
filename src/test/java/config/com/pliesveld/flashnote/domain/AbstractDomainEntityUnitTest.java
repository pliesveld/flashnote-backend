package com.pliesveld.flashnote.domain;

import com.pliesveld.tests.AbstractRepositoryUnitTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionEventListener;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Component
@Configuration
public class AbstractDomainEntityUnitTest extends AbstractRepositoryUnitTest implements ApplicationContextAware
{
    protected static final Logger LOG = LogManager.getLogger("BaseEntityTests");

    protected ApplicationContext ctx;

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

    public AccountPasswordResetToken accountPasswordResetTokenBean()
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
