package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.unit.spring.DefaultTestAnnotations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultTestAnnotations
@Transactional
public class DeckTest
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
        // TODO: StudentDetails, Attachment, Category
    }

    StudentDetails author;

    @Before
    public void initializeDeckOwner()
    {
        Student student = new Student();
        student.setPassword("password");
        student.setEmail("test@example.com");
        entityManager.persist(student);

        author = new StudentDetails();
        author.setName("author");
        author.setStudent(student);
        entityManager.persist(author);
        entityManager.flush();
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

        Deck deck = new Deck(author);
        deck.getFlashCards().add(fc1);
        deck.getFlashCards().add(fc2);

        entityManager.persist(fc1);
        entityManager.persist(fc2);

        entityManager.persist(deck);


        assertEquals("FlashCard count should be 2",2,((Long)cardService.countFlashCards()).intValue());
        assertEquals("Question count should be 1",1,((Long)cardService.countQuestions()).intValue());
        assertEquals("Answer count should be 2",2,((Long)cardService.countAnswers()).intValue());

    }
    
    @Test
    public void createDeck()
    {
        int i = 1;
        int a_no = 0;
        int q_no = 0;

        assertEquals("FlashCard count should be zero",0,((Long)cardService.countFlashCards()).intValue());


        Deck deck = new Deck(author);
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

        assertEquals("Question count should be 5",5,((Long)cardService.countQuestions()).intValue());
        assertEquals("Answer count should be 5",5,((Long)cardService.countAnswers()).intValue());
        assertEquals("FlashCard count should be 5",5,((Long)cardService.countFlashCards()).intValue());
        assertEquals("Deck count should be 1",1,((Long)cardService.countDecks()).intValue());
        
        assertEquals("Deck size should be 5",5,deck.getFlashCards().size());
    }



    @Test
    public void modifyDeck()
    {
        int i = 1;
        int a_no = 0;
        int q_no = 0;

        assertEquals("FlashCard count should be zero",0,((Long)cardService.countFlashCards()).intValue());


        Deck deck = new Deck(author);
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
        entityManager.flush();

        assertEquals("Question count should be 5",5,((Long)cardService.countQuestions()).intValue());
        assertEquals("Answer count should be 5",5,((Long)cardService.countAnswers()).intValue());
        assertEquals("FlashCard count should be 5",5,((Long)cardService.countFlashCards()).intValue());
        assertEquals("Deck count should be 1",1,((Long)cardService.countDecks()).intValue());

        assertEquals("Deck size should be 5",5,deck.getFlashCards().size());

        FlashCard fc_removed = deck.getFlashCards().remove(2);
        entityManager.remove(fc_removed);


        assertEquals("Deck size should be 4",4,deck.getFlashCards().size());
        assertEquals("FlashCard count should be 4",4,((Long)cardService.countFlashCards()).intValue());
    }



    @Test
    public void createDeckPersistLoad()
    {
        int i = 1;
        int a_no = 0;
        int q_no = 0;

        assertEquals("FlashCard count should be zero",0,((Long)cardService.countFlashCards()).intValue());

        Deck deck = new Deck(author);
        deck.setTitle("This is an example Deck.");

        List<FlashCard> list = new LinkedList<>();

        Question que = new Question();
        que.setContent("This is a question?");
        entityManager.persist(que);

        do {
            Answer ans = new Answer();
            ans.setContent(String.format("%d", a_no++));
            entityManager.persist(ans);

            FlashCard fc = new FlashCard(que,ans);

            entityManager.persist(fc);
            entityManager.flush();
            list.add(fc);

        } while(i++ < 5);

        deck.setFlashCards(list);
        entityManager.persist(deck);
        entityManager.flush();

        assertEquals("FlashCard count should be five", 5, ((Long) cardService.countFlashCards()).intValue());

        assertEquals("Deck count should be one",1,((Long)cardService.countDecks()).intValue());

        Serializable deck_id = deck.getId();

        {
            Deck deck2 = entityManager.find(Deck.class, deck_id);
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
            entityManager.merge(deck2);
        }

        deck = null;

        {
            Deck deck3 = entityManager.find(Deck.class, deck_id);

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

