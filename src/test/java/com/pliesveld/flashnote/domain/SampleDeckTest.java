package com.pliesveld.flashnote.domain;

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
        Session session = sessionFactory.getCurrentSession();
        assertNotNull(session);

        assertTrue(session.isOpen());
        assertTrue(session.isConnected());
    }

    @Test
    public void verifyEmpty()
    {
        Session session = sessionFactory.getCurrentSession();
        
        {
            Query query = session.createQuery("SELECT count(*) FROM com.pliesveld.flashnote.domain.Deck");
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


    }

    @Test
    public void createDeck()
    {
        Session session = sessionFactory.getCurrentSession();


        int i = 1;
        int a_no = 0;
        int q_no = 0;

        {
            Criteria crit = session.createCriteria(FlashCard.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 0 flashcards",0,((Long)crit.uniqueResult()).intValue());
        }


        Deck deck = new Deck();
        deck.setTitle("This is an example Deck.");

        List<FlashCard> list = new LinkedList<>();
        do {


            Answer ans = new Answer();
            ans.setContent(String.format("This is an answer no %d", a_no++));
            session.save(ans);

            Question que = new Question();
            que.setContent(String.format("This is question no %d", q_no++));
            session.save(que);


            FlashCard fc = new FlashCard(que,ans);

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


        FlashCard fc_removed = deck.getFlashCards().remove(2);
        session.delete(fc_removed);

        {
            Criteria crit = session.createCriteria(FlashCard.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 4 flashcards",4,((Long)crit.uniqueResult()).intValue());
        }



    }


    @Test
    public void deckOfOneQuestionTwoAnswers()
    {
        Session session = sessionFactory.getCurrentSession();


        Question question = new Question();
        question.setContent("Question?");

        Answer answer1 = new Answer();
        answer1.setContent("This is the first answer");
        Answer answer2 = new Answer();
        answer1.setContent("This is the second answer");

        session.save(question);
        session.save(answer1);
        session.save(answer2);

        FlashCard fc1 = new FlashCard(question,answer1);

        FlashCard fc2 = new FlashCard(question,answer2);

        Deck deck = new Deck();
        deck.getFlashCards().add(fc1);
        deck.getFlashCards().add(fc2);

        session.save(fc1);
        session.save(fc2);

        session.save(deck);

        {
            Criteria crit = session.createCriteria(FlashCard.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 2 flashcards",2,((Long)crit.uniqueResult()).intValue());
        }

        {
            Criteria crit = session.createCriteria(Question.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 1 question",1,((Long)crit.uniqueResult()).intValue());
        }

        {
            Criteria crit = session.createCriteria(Answer.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 2 answers",2,((Long)crit.uniqueResult()).intValue());
        }

/*
        FlashCard fc_returned = deck.getFlashCards().remove(1);
        session.delete(fc_returned);

        session.getTransaction().commit();


        {
            Criteria crit = session.createCriteria(Deck.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 1 deck",1,((Long)crit.uniqueResult()).intValue());
        }

        {
            Criteria crit = session.createCriteria(Question.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 1 question",1,((Long)crit.uniqueResult()).intValue());
        }

        {
            Criteria crit = session.createCriteria(FlashCard.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 1 flashcards",1,((Long)crit.uniqueResult()).intValue());
        }

        {
            Criteria crit = session.createCriteria(Answer.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Database should have 1 answers",1,((Long)crit.uniqueResult()).intValue());
        }
*/



    }

}

