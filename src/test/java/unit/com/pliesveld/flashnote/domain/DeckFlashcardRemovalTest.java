package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.spring.BlankEntityTestAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@BlankEntityTestAnnotations
@Transactional
public class DeckFlashcardRemovalTest extends StudentTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Before
    @Override
    public void setupEntities() {
        super.setupEntities();
    }

    @Test
    public void whenContextLoad_thenCorrect() {

    }

    @After
    public void flushAfter() {
        entityManager.flush();
    }


    @Test
    public void studentDeckRemove() {
        Serializable sid = null;
        Serializable did = null;


        {
            Category category = this.categoryBean();
            entityManager.persist(category);

            Student student1 = new Student();
            student1.setName("Student1");
            student1.setEmail("student1@email.com");
            student1.setPassword("password");
            entityManager.persist(student1);

            Question que = new Question("Question?");
            Answer ans = new Answer("Answer.");

            entityManager.persist(que);
            entityManager.persist(ans);

            FlashCard fc = new FlashCard(que, ans);
            Deck deck = new Deck(category, UUID.randomUUID().toString());
            deck.getFlashcards().add(fc);
            deck.setCategory(category);

            entityManager.persist(student1);
            entityManager.persist(deck);
            entityManager.flush();

            did = deck.getId();
            sid = student1.getId();
        }

        assertNotNull(sid);
        Student student = entityManager.find(Student.class, sid);
        assertNotNull(student);


        Deck deck = entityManager.find(Deck.class, did);


        FlashCard fc = deck.getFlashcards().get(0);

        Question que = fc.getQuestion();
        Answer ans = fc.getAnswer();

        entityManager.remove(fc);

        entityManager.remove(que);
        entityManager.remove(ans);

        entityManager.remove(deck);

        entityManager.flush();
    }

}

