package com.pliesveld.flashnote.domain;

import com.pliesveld.spring.SpringTestConfig;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

import java.io.Serializable;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class StudentDeckTest
{
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void studentDeckConstruct()
    {
        Student student1 = new Student();
        student1.setName("Student1");
        student1.setEmail("student1@email.com");
        student1.setPassword("password");
                
        Question que = new Question("Question?");
        Answer ans = new Answer("Answer.");
        
        entityManager.persist(que);
        entityManager.persist(ans);
        
        FlashCard fc = new FlashCard(que,ans);
        Deck deck = new Deck();
        deck.getFlashCards().add(fc);
        student1.getDecks().add(deck);
        
        entityManager.persist(student1);
        entityManager.flush();

    }
    
    @Test
    public void studentDeckRemove()
    {
        Serializable sid = null; 
        Serializable did = null;
        
        
    
        {
            Student student1 = new Student();
            student1.setName("Student1");
            student1.setEmail("student1@email.com");
            student1.setPassword("password");
                    
            Question que = new Question("Question?");
            Answer ans = new Answer("Answer.");
            
            entityManager.persist(que);
            entityManager.persist(ans);
            
            FlashCard fc = new FlashCard(que,ans);
            Deck deck = new Deck();
            deck.getFlashCards().add(fc);
            student1.getDecks().add(deck);
            
            entityManager.persist(student1);
            entityManager.flush();
            
            did = deck.getId();
            sid = student1.getId();
        }
        
        assertNotNull(sid);
        Student student = entityManager.find(Student.class, sid);
        assertNotNull(student);
        

        Deck deck = entityManager.find(Deck.class,did);
        
        
        
        FlashCard fc = deck.getFlashCards().get(0);
        
        Question que = fc.getQuestion();
        Answer ans = fc.getAnswer();

        entityManager.remove(fc);
        
        entityManager.remove(que);
        entityManager.remove(ans);
        

        

        entityManager.remove(deck);
        
        student.getDecks().clear();
        entityManager.flush();
        
    }

}

