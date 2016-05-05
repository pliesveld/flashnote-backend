package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.spring.DefaultEntityTestAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class DeckTest extends StudentDetailsTest
{
    @PersistenceContext
    EntityManager entityManager;

    private StudentDetails author;
    private Category category;

    @Before
    @Override
    public void setupEntities()
    {
        super.setupEntities();
        assertNotNull(student_id);

        author = entityManager.getReference(StudentDetails.class, student_id);
        assertTrue(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(author));

        category = categoryBean();
        entityManager.persist(category);

    }



    @Test
    public void testEntitySanity()
    {
        assertStudentDetailsRepositoryCount(1);
        assertStudentRepositoryCount(1);
        assertNotNull(student_id);
        assertNotNull(entityManager.find(Student.class,student_id));
        assertNotNull(entityManager.find(StudentDetails.class,student_id));
    }

    @After
    @Override
    public void flushAfter()
    {
        entityManager.flush();
    }

    @Test
    public void constructDeckSharedQuestion()
    {
        Question question = questionBean();
        Answer answer1 = answerBean();
        Answer answer2 = answerBean();

//        entityManager.persist(question);
//        entityManager.persist(answer1);
//        entityManager.persist(answer2);

        FlashCard fc1 = new FlashCard(question,answer1);
        FlashCard fc2 = new FlashCard(question,answer2);

        entityManager.persist(fc1);
        entityManager.persist(fc2);

        Deck deck = new Deck(author);
        deck.getFlashcards().add(fc1);
        deck.getFlashcards().add(fc2);
        deck.setCategory(category);

        entityManager.persist(fc1);
        entityManager.persist(fc2);

        entityManager.persist(deck);

        assertQuestionRepositoryCount(1);
        assertAnswerRepositoryCount(2);
        assertFlashCardRepositoryCount(2);
    }


    @Test
    public void constructDeckMultipleFlashcards()
    {
        int i = 1;
        int a_no = 0;
        int q_no = 0;

        assertFlashCardRepositoryCount(0);


        Deck deck = new Deck(author);
        deck.setDescription("This is an example Deck.");
        deck.setCategory(category);

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

        deck.setFlashcards(list);
        entityManager.persist(deck);

        assertQuestionRepositoryCount(5);
        assertAnswerRepositoryCount(5);
        assertFlashCardRepositoryCount(5);
        assertDeckRepositoryCount(1);

        assertEquals("Deck size should be 5",5,deck.getFlashcards().size());
    }


    @Test
    public void modifyDeck()
    {
        int i = 1;
        int a_no = 0;
        int q_no = 0;

        assertFlashCardRepositoryCount(0);

        Deck deck = new Deck(author);
        deck.setDescription("This is an example Deck.");
        deck.setCategory(category);

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

        deck.setFlashcards(list);
        entityManager.persist(deck);
        entityManager.flush();

        assertQuestionRepositoryCount(5);
        assertAnswerRepositoryCount(5);
        assertFlashCardRepositoryCount(5);
        assertDeckRepositoryCount(1);

        assertEquals("Deck size should be 5",5,deck.getFlashcards().size());

        FlashCard fc_removed = deck.getFlashcards().remove(2);
        entityManager.remove(fc_removed);

        assertEquals("Deck size should be 4",4,deck.getFlashcards().size());
        assertFlashCardRepositoryCount(4);

    }



    @Test
    public void createDeckPersistLoad()
    {
        int i = 1;
        int a_no = 0;
        int q_no = 0;

        assertFlashCardRepositoryCount(0);
//        assertEquals("FlashCard count should be zero",0,((Long)cardService.countFlashCards()).intValue());

        Deck deck = new Deck(author);
        deck.setDescription("This is an example Deck.");
        deck.setCategory(category);

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

        deck.setFlashcards(list);
        entityManager.persist(deck);
        entityManager.flush();

        assertFlashCardRepositoryCount(5);
        assertDeckRepositoryCount(1);

        Serializable deck_id = deck.getId();

        {
            Deck deck2 = entityManager.find(Deck.class, deck_id);
            assertEquals("Decks should be equal",deck,deck2);

            assertNotNull("Loading a deck had a null list",deck2.getFlashcards());
            assertFalse("Loaded deck should have elements",deck2.getFlashcards().isEmpty());
            assertEquals("Loaded deck should have 5 elements", 5, deck2.getFlashcards().size());


            List<FlashCard> deck_list = deck2.getFlashcards();

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

            assertNotNull("Loading a deck had a null list",deck3.getFlashcards());
            assertFalse("Loaded deck should have elements",deck3.getFlashcards().isEmpty());
            assertEquals("Loaded deck should have 5 elements",5,deck3.getFlashcards().size());


            List<FlashCard> deck_list = deck3.getFlashcards();

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

