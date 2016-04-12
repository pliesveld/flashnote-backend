package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.Student;
import com.pliesveld.flashnote.domain.StudentDetails;
import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class StudentDetailsTest extends StudentTest {
    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext
    protected EntityManager entityManager;

    @Before
    @Override
    public void setupEntities()
    {
        super.setupEntities();

        assertNotNull(student_id);

        Student student = entityManager.getReference(Student.class, student_id);
        assertTrue(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(student));

        StudentDetails studentDetails = studentDetailsBean();
        studentDetails.setStudent(student);
        entityManager.persist(studentDetails);

    }

    @Test
    public void testEntitySanity()
    {
        assertStudentRepositoryCount(1);
        assertStudentDetailsRepositoryCount(1);
    }

    @After
    @Override
    public void flushAfter()
    {
        entityManager.flush();
    }

    @Test
    public void removalDetailsCascadesToAccountTest()
    {
        assertStudentRepositoryCount(1);
        assertStudentDetailsRepositoryCount(1);

        StudentDetails studentDetails = studentDetailsRepository.findAll().iterator().next();
//        studentDetailsRepository.delete(studentDetails);
        assertTrue(entityManager.contains(studentDetails));
        entityManager.remove(studentDetails);
        entityManager.flush();

        assertStudentDetailsRepositoryCount(0);
        assertStudentRepositoryCount(0);
    }

    @Test
    public void removalAccountCascadesToDetailsTest()
    {
        assertStudentRepositoryCount(1);
        assertStudentDetailsRepositoryCount(1);

        Student student = studentRepository.findAll().iterator().next();
        studentRepository.delete(student);
        entityManager.flush();

        assertStudentRepositoryCount(0);
        assertStudentDetailsRepositoryCount(0);
    }

    /*
    @Test
    public void createStudentDeck() {

        StudentDetails studentDetails = entityManager.find(StudentDetails.class, student_id);
        assertNotNull(studentDetails);

        Question q = new Question("q");
        Answer a = new Answer("a");
        entityManager.persist(q);
        entityManager.persist(a);
        FlashCard fc = new FlashCard(q, a);
        entityManager.persist(fc);

        Deck deck = new Deck(studentDetails);
        deck.getFlashcards().add(fc);

        entityManager.persist(deck);
        entityManager.flush();

        assertDeckRepositoryCount(1);
        assertQuestionRepositoryCount(1);
        assertAnswerRepositoryCount(1);
        assertFlashCardRepositoryCount(1);
    }
    */

    /*
    @Test
    public void studentIdentityTest() {
        StudentDetails studentDetails1 = entityManager.find(StudentDetails.class, student1_id);
        StudentDetails studentDetails2 = entityManager.find(StudentDetails.class, student2_id);

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

    }*/



    /*

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

*/

}

