package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.repository.DeckRepository;
import com.pliesveld.flashnote.repository.StudentDetailsRepository;
import com.pliesveld.flashnote.repository.StudentRepository;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.unit.spring.DefaultTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultTestAnnotations
@Transactional
public class StudentDetailsTest {
    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    CardService cardService;

    @Autowired
    StudentDetailsRepository studentDetailsRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    DeckRepository deckRepository;

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

    StudentDetails studentDetails;
    StudentDetails studentDetails1;
    StudentDetails studentDetails2;

    Serializable student_id = null;
    Serializable student1_id = null;
    Serializable student2_id = null;

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

        studentDetails = new StudentDetails();
        student.setStudentDetails(studentDetails);
        studentDetails.setStudent(student);
        studentDetails.setName("studentDetails");
        entityManager.persist(studentDetails);

        studentDetails1 = new StudentDetails();
        student1.setStudentDetails(studentDetails1);
        studentDetails1.setStudent(student1);
        studentDetails1.setName("studentDetails1");
        entityManager.persist(studentDetails1);

        studentDetails2 = new StudentDetails();
        student2.setStudentDetails(studentDetails2);
        studentDetails2.setStudent(student2);
        studentDetails2.setName("studentDetails2");
        entityManager.persist(studentDetails2);
        entityManager.flush();

        student_id = student.getId();
        student1_id = student1.getId();
        student2_id = student2.getId();
    }

    @After
    public void removeStudent()
    {
        student = null;
        student1 = null;
        student2 = null;
        studentDetails = null;
        studentDetails1 = null;
        studentDetails2 = null;
        student_id = null;
        student1_id = null;
        student2_id = null;
    }

    @Test
    public void createStudentDeck() {

        StudentDetails studentDetails = entityManager.find(StudentDetails.class,student_id);
        assertNotNull(studentDetails);

        Question q = new Question("q");
        Answer a = new Answer("a");
        entityManager.persist(q);
        entityManager.persist(a);
        FlashCard fc = new FlashCard(q, a);
        entityManager.persist(fc);

        Deck deck = new Deck(studentDetails);
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
    public void studentIdentityTest() {
        StudentDetails studentDetails1 = entityManager.find(StudentDetails.class,student1_id);
        StudentDetails studentDetails2 = entityManager.find(StudentDetails.class,student2_id);

        Serializable s1 = studentDetails1.getId();
        Serializable s2 = studentDetails2.getId();

        assertTrue(s1 != s2);

        assertFalse(studentDetails1 == studentDetails2);
        assertFalse(studentDetails1.equals(studentDetails2));
        assertFalse(studentDetails2.equals(studentDetails1));

        StudentDetails copy_studentDetails1 = entityManager.find(StudentDetails.class, s1);
        assertTrue(studentDetails1.equals(copy_studentDetails1));
        assertTrue(copy_studentDetails1.equals(studentDetails1));



        assertTrue(studentDetails1.getName() == copy_studentDetails1.getName());

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
        entityManager.flush();
        return fc;
    }
    
    @Test
    public void studentDeckIdentityTest() {

        List<Deck> student_decks = new ArrayList<>();

        student_decks.addAll(Arrays.asList(
                new Deck(studentDetails1, newFlashCard("Que1","Ans1")),
                new Deck(studentDetails1, newFlashCard("Que2","Ans2")),
                new Deck(studentDetails1, newFlashCard("Que3","Ans3"))
        ));

        student_decks.forEach(entityManager::persist);

        entityManager.flush();

        StudentDetails copy_studentDetails1 = entityManager.find(StudentDetails.class, student1_id);

//        deckRepository.findByAuthor_Id(copy_studentDetails1.getId()).forEach((deck) -> LOG.info("{}",deck));

        assertEquals(3, deckRepository.findByAuthor_Id(copy_studentDetails1.getId()).size());

        Set<Deck> set_copy = new HashSet<Deck>();
        set_copy.addAll(deckRepository.findByAuthor_Id(copy_studentDetails1.getId()));

        assertFalse(set_copy.isEmpty());
        assertEquals(3,set_copy.size());
    }

    @Test
    public void removalDetailsCascadesToAccountTest()
    {
        assertEquals(3,studentRepository.count());
        assertEquals(3,studentDetailsRepository.count());

        studentDetailsRepository.delete(studentDetails);
        entityManager.flush();

        assertEquals(2,studentRepository.count());
        assertEquals(2,studentDetailsRepository.count());
    }


    @Test
    public void removalAccountCascadesToDetailsTest()
    {
        assertEquals(3,studentRepository.count());
        assertEquals(3,studentDetailsRepository.count());

        studentRepository.delete(student);

        entityManager.flush();

        assertEquals(2,studentRepository.count());
        assertEquals(2,studentDetailsRepository.count());

    }
}

