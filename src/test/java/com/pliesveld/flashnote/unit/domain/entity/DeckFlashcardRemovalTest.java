package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class DeckFlashcardRemovalTest extends StudentDetailsTest
{
    @PersistenceContext
    private EntityManager entityManager;

    @Before
    @Override
    public void setupEntities()
    {
        super.setupEntities();
    }

    @Test
    public void testEntitySanity()
    {

    }

    @After
    @Override
    public void flushAfter()
    {
        entityManager.flush();
    }


    @Test
    public void studentDeckRemove()
    {
        Serializable sid = null; 
        Serializable did = null;
        
        
    
        {
            StudentDetails studentDetails1 = new StudentDetails();
            studentDetails1.setName("Student1");
            Student student1 = new Student();
            student1.setEmail("studentDetails1@email.com");
            student1.setPassword("password");
            entityManager.persist(student1);
            studentDetails1.setStudent(student1);
                    
            Question que = new Question("Question?");
            Answer ans = new Answer("Answer.");
            
            entityManager.persist(que);
            entityManager.persist(ans);
            
            FlashCard fc = new FlashCard(que,ans);
            Deck deck = new Deck(studentDetails1);
            deck.getFlashCards().add(fc);

            entityManager.persist(studentDetails1);
            entityManager.persist(deck);
            entityManager.flush();
            
            did = deck.getId();
            sid = studentDetails1.getId();
        }
        
        assertNotNull(sid);
        StudentDetails studentDetails = entityManager.find(StudentDetails.class, sid);
        assertNotNull(studentDetails);
        

        Deck deck = entityManager.find(Deck.class,did);
        
        
        FlashCard fc = deck.getFlashCards().get(0);
        
        Question que = fc.getQuestion();
        Answer ans = fc.getAnswer();

        entityManager.remove(fc);
        
        entityManager.remove(que);
        entityManager.remove(ans);

        entityManager.remove(deck);

        entityManager.flush();
    }

}

