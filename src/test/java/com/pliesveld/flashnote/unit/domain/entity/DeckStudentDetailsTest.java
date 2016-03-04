package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.unit.spring.DefaultTestAnnotations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultTestAnnotations
@Transactional
public class DeckStudentDetailsTest
{
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void studentDeckConstruct()
    {
        Student student = new Student();
        StudentDetails studentDetails1 = new StudentDetails();
        studentDetails1.setName("Student1");
        student.setEmail("studentDetails1@email.com");
        student.setPassword("password");
        studentDetails1.setStudent(student);


                
        Question que = new Question("Question?");
        Answer ans = new Answer("Answer.");
        
        entityManager.persist(que);
        entityManager.persist(ans);
        
        FlashCard fc = new FlashCard(que,ans);
        Deck deck = new Deck(studentDetails1);
        deck.getFlashCards().add(fc);
        
        entityManager.persist(studentDetails1);
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

