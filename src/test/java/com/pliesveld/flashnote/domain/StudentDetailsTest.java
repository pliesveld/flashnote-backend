package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.repository.StudentDetailsRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.spring.DefaultTestAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultTestAnnotations
@Transactional
public class StudentDetailsTest {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CardService cardService;

    @Autowired
    StudentDetailsRepository studentDetailsRepository;

    @Autowired
    StudentRepository studentRepository;

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
        // TODO: StudentDetails, Attachment, Category
    }

    Student student;
    Student student1;
    Student student2;


    @Before
    public void createStudent()
    {
        student = new Student();
        student.setEmail("email@example.com");
        student.setPassword("password");
        entityManager.persist(student);

        student1 = new Student();
        student1.setEmail("student1@example.com");
        student1.setPassword("password");
        entityManager.persist(student1);

        student2 = new Student();
        student2.setEmail("student2@example.com");
        student2.setPassword("password");
        entityManager.persist(student2);

    }

    @After
    public void removeStudent()
    {
        student = null;
        student1 = null;
        student2 = null;
    }

    @Test
    public void createStudentDeck() {

        StudentDetails studentDetails = new StudentDetails();
        studentDetails.setName("StudentDetails");


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
        //studentDetails.addDeck(deck);

    }

    @Test
    public void createStudentDeckCascade() {
        StudentDetails studentDetails = new StudentDetails();
        studentDetails.setName("StudentDetails");
//        studentDetails.setPassword("password");
//        studentDetails.setEmail("studentDetails@example.com");

        Deck deck = new Deck();
        Question que = new Question("q");
        Answer ans = new Answer("a");
        entityManager.persist(que);
        entityManager.persist(ans);    
        
        deck.getFlashCards().add(new FlashCard(que,ans));

        studentDetails.getDecks().add(deck);
        studentDetails.setStudent(student);
        entityManager.persist(studentDetails);
        entityManager.flush();
        Serializable student_id = studentDetails.getId();

        deck = null;
        studentDetails = null;

        studentDetails = entityManager.find(StudentDetails.class, student_id);
        assertNotNull(studentDetails);
        assertNotNull(studentDetails.getDecks());
        assertFalse(studentDetails.getDecks().isEmpty());
        assertEquals(1, studentDetails.getDecks().size());
        deck = studentDetails.getDecks().iterator().next();
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
        StudentDetails studentDetails1 = new StudentDetails("student");
        StudentDetails studentDetails2 = new StudentDetails("student");

        studentDetails1.setStudent(student1);
        studentDetails2.setStudent(student2);

        entityManager.persist(studentDetails1);
        entityManager.persist(studentDetails2);
        entityManager.flush();

        Serializable s1 = studentDetails1.getId();
        Serializable s2 = studentDetails2.getId();

        assertFalse(studentDetails1 == studentDetails2);
        assertFalse(studentDetails1.equals(studentDetails2));
        assertFalse(studentDetails2.equals(studentDetails1));

        StudentDetails copy_studentDetails1 = entityManager.find(StudentDetails.class, s1);
        assertTrue(studentDetails1.equals(copy_studentDetails1));
        assertTrue(copy_studentDetails1.equals(studentDetails1));

        assertTrue(s1 != s2);

        assertTrue(studentDetails1.getName().equals(studentDetails2.getName()));
        assertTrue(studentDetails1.getName() == copy_studentDetails1.getName()); //questionable
        assertTrue(studentDetails1.getName() == studentDetails2.getName());

        assertTrue(copy_studentDetails1.getName().equals(studentDetails1.getName()));

        studentDetails1.setName("otherstudent");       // property has shared reference
        assertTrue(copy_studentDetails1.getName().equals("otherstudent"));

        assertFalse(studentDetails1.getName().equals(studentDetails2.getName()));
        assertFalse(studentDetails1.getName() == studentDetails2.getName());

        assertTrue(studentDetails1.equals(copy_studentDetails1));
        assertTrue(copy_studentDetails1.equals(studentDetails1));

        assertTrue(copy_studentDetails1.getName().equals(studentDetails1.getName()));

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
        StudentDetails studentDetails1 = new StudentDetails("student");
//        studentDetails1.setPassword("password");
//        studentDetails1.setEmail("studentDetails1@example.com");

        HashSet<Deck> student_decks = new LinkedHashSet<>();

        student_decks.addAll(Arrays.asList(
                new Deck(newFlashCard("Que1","Ans1")),
                new Deck(newFlashCard("Que2","Ans2")),
                new Deck(newFlashCard("Que3","Ans3"))
        ));

        studentDetails1.setStudent(student1);
        entityManager.persist(studentDetails1);
        studentDetails1.setDecks(student_decks);

        entityManager.flush();
        Serializable s1 = studentDetails1.getId();


        StudentDetails copy_studentDetails1 = entityManager.find(StudentDetails.class, s1);

        Set<?> set_orig = studentDetails1.getDecks();
        Set<?> set_copy = copy_studentDetails1.getDecks();

        assertFalse(set_orig.isEmpty());
        assertEquals(set_orig.size(), set_copy.size());

    }

    @Test
    public void removalDetailsCascadesToAccountTest()
    {
        assertEquals(3,studentRepository.count());
        assertEquals(0,studentDetailsRepository.count());

        StudentDetails studentDetails = new StudentDetails();
        studentDetails.setName("name");
        studentDetails.setStudent(student);
        studentDetailsRepository.save(studentDetails);
        entityManager.flush();

        assertEquals(3,studentRepository.count());
        assertEquals(1,studentDetailsRepository.count());

        entityManager.clear();

        studentDetailsRepository.delete(studentDetails);
        entityManager.flush();

        assertEquals(2,studentRepository.count());
        assertEquals(0,studentDetailsRepository.count());

    }


    @Test
    public void removalAccountCascadesToDetailsTest()
    {
        assertEquals(3,studentRepository.count());
        assertEquals(0,studentDetailsRepository.count());

        StudentDetails studentDetails = new StudentDetails();
        studentDetails.setName("name");
        studentDetails.setStudent(student);
        studentDetailsRepository.save(studentDetails);
        entityManager.flush();

        assertEquals(3,studentRepository.count());
        assertEquals(1,studentDetailsRepository.count());

        entityManager.clear();

        studentRepository.delete(student);
        entityManager.flush();

        assertEquals(2,studentRepository.count());
        assertEquals(0,studentDetailsRepository.count());

    }
}

