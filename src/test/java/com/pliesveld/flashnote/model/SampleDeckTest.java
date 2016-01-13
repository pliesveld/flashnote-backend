package com.pliesveld.flashnote.model;

import com.pliesveld.config.SpringTestConfig;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class SampleDeckTest
{
    @Autowired
    SessionFactory sessionFactory;

    @Before
    public void setup()
    {
        /*
         * ApplicationContext ctx = new
         * AnnotationConfigApplicationContext(SpringConfig.class);
         * LocalSessionFactoryBean sfb = (LocalSessionFactoryBean)
         * ctx.getBean("&sessionFactory"); sessionFactory =
         * sfb.getConfiguration().buildSessionFactory();
         */
    }

    @Test
    public void sessionFactoryWired()
    {
        assertNotNull(sessionFactory);
    }

    @Test
    public void sessionHasCurrent()
    {
        Session session = sessionFactory.openSession();
        assertNotNull(session);

        assertTrue(session.isOpen());
        assertTrue(session.isConnected());
        session.close();
    }

    @Test
    public void verifyEmpty()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction().begin();
        {
            Query query = session.createQuery("SELECT count(*) FROM com.pliesveld.flashnote.model.Deck");
            Long count = (Long)query.uniqueResult();
            assertTrue("Deck is zero",count == 0);
        }

        {
            Criteria crit = session.createCriteria(Deck.class);
            crit.setProjection(Projections.rowCount());
            Long count_long = (Long)crit.uniqueResult();
            int count = count_long.intValue();
            assertEquals("Database rows should be empty",0,count);
        }

        Class<?>[] classArray = {Student.class,Question.class,Answer.class,FlashCard.class,Deck.class,Category.class};

        for(Class<?> tableClass : classArray)
        {
            Criteria crit = session.createCriteria(tableClass);
            crit.setProjection(Projections.rowCount());
            Long count_long = (Long)crit.uniqueResult();
            int count = count_long.intValue();
            assertEquals("Database rows should be empty",0,count);

        }

        session.getTransaction().commit();
    }


    private void clearDeck()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction().begin();
        SQLQuery sql;

        sql = session.createSQLQuery("DELETE FROM DECK_FLASHCARD;");
        sql.executeUpdate();

        sql = session.createSQLQuery("DELETE FROM DECK;");
        sql.executeUpdate();

        sql = session.createSQLQuery("DELETE FROM FLASHCARD;");
        sql.executeUpdate();

        sql = session.createSQLQuery("DELETE FROM QUESTION;");
        sql.executeUpdate();
        sql = session.createSQLQuery("DELETE FROM ANSWER;");
        sql.executeUpdate();

        session.getTransaction().commit();
    }

    @Test
    public void createDeck()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        int i = 1;
        int a_no = 0;
        int q_no = 0;


        Deck deck = new Deck();
        deck.setTitle("This is an example Deck.");

        List<FlashCard> list = new LinkedList<>();
        do {
            FlashCard fc = new FlashCard();

            Answer ans = new Answer();
            ans.setContent(String.format("This is an answer no %d", a_no++));
            fc.setAnswer(ans);

            Question que = new Question();
            que.setContent(String.format("This is question no %d", q_no++));
            fc.setQuestion(que);

            session.save(fc);
            list.add(fc);

        } while(i++ < 5);

        deck.setFlashCards(list);
        session.save(deck);

        {
            Criteria crit = session.createCriteria(FlashCard.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 5 flashcards",5,((Long)crit.uniqueResult()).intValue());
        }

        {
            Criteria crit = session.createCriteria(Deck.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 1 deck",1,((Long)crit.uniqueResult()).intValue());
        }


        deck.getFlashCards().remove(2);

        session.save(deck);

        {
            Criteria crit = session.createCriteria(FlashCard.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 4 flashcards",4,((Long)crit.uniqueResult()).intValue());
        }

        session.getTransaction().commit();


    }


}

