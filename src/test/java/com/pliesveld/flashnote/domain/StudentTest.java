package com.pliesveld.flashnote.domain;

import com.pliesveld.spring.SpringTestConfig;
import com.pliesveld.flashnote.service.CardService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class StudentTest {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CardService cardService;

    @Test
    public void entityManagerWired() {
        assertNotNull(entityManager);
    }

    @Test
    public void verifyEmpty() {
        assertEquals("Deck count should be zero", 0, ((Long) cardService.countDecks()).intValue());
        assertEquals("Question count should be zero", 0, ((Long) cardService.countQuestions()).intValue());
        assertEquals("Answer count should be zero", 0, ((Long) cardService.countAnswers()).intValue());
        assertEquals("FlashCard count should be zero", 0, ((Long) cardService.countFlashCards()).intValue());
        // TODO: Student, Attachment, Category
    }


    @Test
    public void createStudentDeck() {

        Student student = new Student();
        student.setName("Student");

        Question q = new Question("q");
        Answer a = new Answer("a");
        entityManager.persist(q);
        entityManager.persist(a);
        FlashCard fc = new FlashCard(q, a);
        entityManager.persist(fc);

        Deck deck = new Deck();
        deck.getFlashCards().add(fc);


        entityManager.persist(deck);
        entityManager.flush();


        assertEquals("Deck count should be zero", 1, ((Long) cardService.countDecks()).intValue());
        assertEquals("Question count should be zero", 1, ((Long) cardService.countQuestions()).intValue());
        assertEquals("Answer count should be zero", 1, ((Long) cardService.countAnswers()).intValue());
        assertEquals("FlashCard count should be zero", 1, ((Long) cardService.countFlashCards()).intValue());
        //student.addDeck(deck);

    }

    @Test
    public void createStudentDeckCascade() {
        Student student = new Student();
        student.setName("Student");
        student.setPassword("password");
        student.setEmail("student@example.com");

        Deck deck = new Deck();
        Question que = new Question("q");
        Answer ans = new Answer("a");
        entityManager.persist(que);
        entityManager.persist(ans);    
        
        deck.getFlashCards().add(new FlashCard(que,ans));

        student.getDecks().add(deck);
        entityManager.persist(student);
        entityManager.flush();
        Serializable student_id = student.getId();

        deck = null;
        student = null;

        student = entityManager.find(Student.class, student_id);
        assertNotNull(student);
        assertNotNull(student.getDecks());
        assertFalse(student.getDecks().isEmpty());
        assertEquals(1, student.getDecks().size());
        deck = student.getDecks().iterator().next();
        assertNotNull(deck);
        assertNotNull(deck.getFlashCards());
        assertFalse(deck.getFlashCards().isEmpty());
        assertEquals(1, deck.getFlashCards().size());
        FlashCard fc = deck.getFlashCards().iterator().next();
        assertNotNull(fc);
        assertNotNull(fc.getQuestion());
        assertNotNull(fc.getAnswer());

    }


    @Test
    public void studentIdentityTest() {
        Student student1 = new Student("student");
        Student student2 = new Student("student");

        student1.setEmail("student1@example.com");
        student2.setEmail("student2@example.com");
        student1.setPassword("password");
        student2.setPassword("password");
        
        entityManager.persist(student1);
        entityManager.persist(student2);
        entityManager.flush();

        Serializable s1 = student1.getId();
        Serializable s2 = student2.getId();

        assertFalse(student1 == student2);
        assertFalse(student1.equals(student2));
        assertFalse(student2.equals(student1));

        Student copy_student1 = entityManager.find(Student.class, s1);
        assertTrue(student1.equals(copy_student1));
        assertTrue(copy_student1.equals(student1));

        assertTrue(s1 != s2);

        assertTrue(student1.getName().equals(student2.getName()));
        assertTrue(student1.getName() == copy_student1.getName()); //questionable
        assertTrue(student1.getName() == student2.getName());

        assertTrue(copy_student1.getName().equals(student1.getName()));

        student1.setName("otherstudent");       // property has shared reference
        assertTrue(copy_student1.getName().equals("otherstudent"));

        assertFalse(student1.getName().equals(student2.getName()));
        assertFalse(student1.getName() == student2.getName());

        assertTrue(student1.equals(copy_student1));
        assertTrue(copy_student1.equals(student1));

        assertTrue(copy_student1.getName().equals(student1.getName()));

    }

    @Transactional
    private FlashCard newFlashCard(String question, String answer)
    {
        Question que = new Question(question);
        Answer ans = new Answer(answer);
        entityManager.persist(que);
        entityManager.persist(ans);
        entityManager.flush();
        
        FlashCard fc = new FlashCard(que,ans);
        entityManager.persist(fc);
        return fc;
    }
    
    @Test
    public void studentDeckIdentityTest() {
        Student student1 = new Student("student");
        student1.setPassword("password");
        student1.setEmail("student1@example.com");

        HashSet<Deck> student_decks = new LinkedHashSet<>();

        student_decks.addAll(Arrays.asList(
                new Deck(newFlashCard("Que1","Ans1")),
                new Deck(newFlashCard("Que2","Ans2")),
                new Deck(newFlashCard("Que3","Ans3"))
        ));

        entityManager.persist(student1);
        student1.setDecks(student_decks);

        entityManager.flush();
        Serializable s1 = student1.getId();


        Student copy_student1 = entityManager.find(Student.class, s1);

        Set<?> set_orig = student1.getDecks();
        Set<?> set_copy = copy_student1.getDecks();

        assertFalse(set_orig.isEmpty());
        assertEquals(set_orig.size(), set_copy.size());

    }
}

