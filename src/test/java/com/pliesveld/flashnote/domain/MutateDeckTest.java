package com.pliesveld.flashnote.domain;

import com.pliesveld.config.SpringTestConfig;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class MutateDeckTest
{
    @Autowired
    SessionFactory sessionFactory;

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

        session.getTransaction().commit();
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

        Question que = new Question();
        que.setContent("This is a question?");
        session.save(que);

        do {
            Answer ans = new Answer();
            ans.setContent(String.format("%d", a_no++));
            session.save(ans);

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

        Serializable deck_id = deck.getId();

        {
            Deck deck2 = session.load(Deck.class,deck_id);
            assertEquals("Decks should be equal",deck,deck2);

            assertNotNull("Loading a deck had a null list",deck2.getFlashCards());
            assertFalse("Loaded deck should have elements",deck2.getFlashCards().isEmpty());
            assertEquals("Loaded deck should have 5 elements", 5, deck2.getFlashCards().size());


            List<FlashCard> deck_list = deck2.getFlashCards();

            String expected_string[] = {"0","1","2","3","4"};

            int j = 0;
            for(FlashCard fc : deck_list)
            {
                assertEquals("Fetched answer differed", expected_string[j], fc.getAnswer().getContent());
                j++;
            }

            FlashCard fc_move = deck_list.remove(1);
            deck_list.add(fc_move);
            session.saveOrUpdate(deck2);

        }

        deck = null;

        {
            Deck deck3 = session.load(Deck.class,deck_id);

            assertNotNull("Loading a deck had a null list",deck3.getFlashCards());
            assertFalse("Loaded deck should have elements",deck3.getFlashCards().isEmpty());
            assertEquals("Loaded deck should have 5 elements",5,deck3.getFlashCards().size());


            List<FlashCard> deck_list = deck3.getFlashCards();

            String expected_string[] = {"0","2","3","4","1"};

            int j = 0;
            for(FlashCard fc : deck_list)
            {
                assertEquals("Fetched answer differed", expected_string[j], fc.getAnswer().getContent());
                j++;
            }

        }



        
    }



}

