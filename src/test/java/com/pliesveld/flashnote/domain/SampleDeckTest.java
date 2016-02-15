package com.pliesveld.flashnote.domain;

import com.pliesveld.spring.SpringTestConfig;
import com.pliesveld.flashnote.service.CardService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class SampleDeckTest
{
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CardService cardService;

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
    }

    @Test
    public void entityManagerWired()
    {
        assertNotNull(entityManager);
    }

    @Test
    public void verifyEmpty()
    {
        assertEquals("Deck count should be zero",0,((Long)cardService.countDecks()).intValue());
        assertEquals("Question count should be zero",0,((Long)cardService.countQuestions()).intValue());
        assertEquals("Answer count should be zero",0,((Long)cardService.countAnswers()).intValue());
        assertEquals("FlashCard count should be zero",0,((Long)cardService.countFlashCards()).intValue());
        // TODO: Student, Attachment, Category
    }

    @Test
    public void createDeck()
    {
        int i = 1;
        int a_no = 0;
        int q_no = 0;

        assertEquals("FlashCard count should be zero",0,((Long)cardService.countFlashCards()).intValue());


        Deck deck = new Deck();
        deck.setTitle("This is an example Deck.");

        List<FlashCard> list = new LinkedList<>();
        do {


            Answer ans = new Answer();
            ans.setContent(String.format("This is an answer no %d", a_no++));
            entityManager.persist(ans);

            Question que = new Question();
            que.setContent(String.format("This is question no %d", q_no++));
            entityManager.persist(que);


            FlashCard fc = new FlashCard(que,ans);

            entityManager.persist(fc);
            list.add(fc);

        } while(i++ < 5);

        deck.setFlashCards(list);
        entityManager.persist(deck);

        assertEquals("FlashCard count should be 5",5,((Long)cardService.countFlashCards()).intValue());
        assertEquals("Deck count should be 1",1,((Long)cardService.countDecks()).intValue());


        FlashCard fc_removed = deck.getFlashCards().remove(2);
        entityManager.remove(fc_removed);

        assertEquals("FlashCard count should be 4",4,((Long)cardService.countFlashCards()).intValue());
    }


    @Test
    public void deckOfOneQuestionTwoAnswers()
    {
        Question question = new Question();
        question.setContent("Question?");

        Answer answer1 = new Answer();
        answer1.setContent("This is the first answer");
        Answer answer2 = new Answer();
        answer1.setContent("This is the second answer");

        entityManager.persist(question);
        entityManager.persist(answer1);
        entityManager.persist(answer2);

        FlashCard fc1 = new FlashCard(question,answer1);

        FlashCard fc2 = new FlashCard(question,answer2);

        Deck deck = new Deck();
        deck.getFlashCards().add(fc1);
        deck.getFlashCards().add(fc2);

        entityManager.persist(fc1);
        entityManager.persist(fc2);

        entityManager.persist(deck);


        assertEquals("FlashCard count should be 2",2,((Long)cardService.countFlashCards()).intValue());
        assertEquals("Question count should be 1",1,((Long)cardService.countQuestions()).intValue());
        assertEquals("Answer count should be 2",2,((Long)cardService.countAnswers()).intValue());


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

