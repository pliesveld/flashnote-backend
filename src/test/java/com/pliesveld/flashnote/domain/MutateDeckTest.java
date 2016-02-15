package com.pliesveld.flashnote.domain;

import com.pliesveld.spring.SpringTestConfig;
import com.pliesveld.flashnote.service.CardService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
public class MutateDeckTest
{
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CardService cardService;

    @Test
    public void entityManagerWired()
    {
        assertNotNull(entityManager);
    }

    @Test
    public void verifyEmpty()
    {
        assertEquals("Deck count should be zero", 0, ((Long) cardService.countDecks()).intValue());
        assertEquals("Question count should be zero", 0, ((Long) cardService.countQuestions()).intValue());
        assertEquals("Answer count should be zero", 0, ((Long) cardService.countAnswers()).intValue());
        assertEquals("FlashCard count should be zero", 0, ((Long) cardService.countFlashCards()).intValue());
        // TODO: Student, Attachment, Category
    }

    @Test
    @Transactional
    public void createDeck()
    {
        int i = 1;
        int a_no = 0;
        int q_no = 0;

        assertEquals("FlashCard count should be zero",0,((Long)cardService.countFlashCards()).intValue());

        Deck deck = new Deck();
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

