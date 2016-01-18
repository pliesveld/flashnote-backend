package com.pliesveld.flashnote.domain;

import com.pliesveld.config.SpringTestConfig;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
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
public class StudentDeckTest
{
    @Autowired
    SessionFactory sessionFactory;

    @Test
    public void sessionFactoryWired()
    {
        assertNotNull(sessionFactory);
    }

    @Test
    public void sessionHasCurrent()
    {
        Session session = sessionFactory.getCurrentSession();
        assertNotNull(session);

        assertTrue(session.isOpen());
        assertTrue(session.isConnected());
    }

    @Test
    public void verifyEmpty()
    {
        Session session = sessionFactory.getCurrentSession();

        {
            Criteria crit = session.createCriteria(Deck.class);
            crit.setProjection(Projections.rowCount());
            assertEquals("Deck is zero", 0, ((Long) crit.uniqueResult()).intValue());
        }

        {
            Criteria crit = session.createCriteria(Deck.class);
            crit.setProjection(Projections.rowCount());
            Long count_long = (Long)crit.uniqueResult();
            int count = count_long.intValue();
            assertEquals("Database rows should be empty",0,count);
        }

        Class<?>[] classArray = {Student.class,Question.class,Answer.class,FlashCard.class,Deck.class,Category.class};

        for(Class<?> tableClass : classArray)
        {
            Criteria crit = session.createCriteria(tableClass);
            crit.setProjection(Projections.rowCount());
            Long count_long = (Long)crit.uniqueResult();
            int count = count_long.intValue();
            assertEquals("Database rows should be empty",0,count);

        }

    }

    @Test
    public void createStudentDeck()
    {
       Session session = sessionFactory.getCurrentSession();

       Student student = new Student();
       student.setName("Student");

       Deck deck = new Deck();
       deck.getFlashCards().add(new FlashCard(new Question("q"),new Answer("a")));
       session.save(deck);

        //student.addDeck(deck);

    }

    @Test
    public void createStudentDeckCascade()
    {
        Session session = sessionFactory.getCurrentSession();

        Student student = new Student();
        student.setName("Student");
        student.setEmail("student@example.com");

        Deck deck = new Deck();
        deck.getFlashCards().add(new FlashCard(new Question("q"),new Answer("a")));

        student.getDecks().add(deck);
        Serializable student_id = session.save(student);

        deck = null;
        student = null;

        student = session.load(Student.class,student_id);
        assertNotNull(student);
        assertNotNull(student.getDecks());
        assertFalse(student.getDecks().isEmpty());
        assertEquals(1,student.getDecks().size());
        deck = student.getDecks().iterator().next();
        assertNotNull(deck);
        assertNotNull(deck.getFlashCards());
        assertFalse(deck.getFlashCards().isEmpty());
        assertEquals(1,deck.getFlashCards().size());
        FlashCard fc = deck.getFlashCards().iterator().next();
        assertNotNull(fc);
        assertNotNull(fc.getQuestion());
        assertNotNull(fc.getAnswer());

    }


    @Test
    public void studentIdentityTest()
    {
        Session session = sessionFactory.getCurrentSession();

        Student student1 = new Student("student");
        Student student2 = new Student("student");

        student1.setEmail("student1@example.com");
        student2.setEmail("student2@example.com");

        Serializable s1 = session.save(student1);
        Serializable s2 = session.save(student2);

        assertFalse(student1 == student2);
        assertFalse(student1.equals(student2));
        assertFalse(student2.equals(student1));

        Student copy_student1 = session.load(Student.class,s1);
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

    @Test
    public void studentDeckIdentityTest()
    {
        Session session = sessionFactory.getCurrentSession();

        Student student1 = new Student("student");
        student1.setEmail("student1@example.com");

        HashSet<Deck> student_decks = new LinkedHashSet<>();

        student_decks.addAll(Arrays.asList(
                new Deck(new FlashCard(new Question("Que1"), new Answer("Ans1"))),
                new Deck(new FlashCard(new Question("Que2"), new Answer("Ans2"))),
                new Deck(new FlashCard(new Question("Que3"), new Answer("Ans3")))
        ));

        student1.setDecks(student_decks);

        Serializable s1 = session.save(student1);


        Student copy_student1 = session.load(Student.class,s1);

        Set<?> set_orig = student1.getDecks();
        Set<?> set_copy = copy_student1.getDecks();

        assertFalse(set_orig.isEmpty());
        assertEquals(set_orig.size(),set_copy.size());

        assertEquals(set_orig,set_copy);
        assertEquals(set_copy,set_orig);

        FlashCard fc_specific = (((Deck) set_copy.iterator().next()).getFlashCards().iterator().next());

        // does original reference hold after persisting
        //assertTrue(set_copy.contains(fc_specific)); //no

//        assertTrue(set_orig.contains(fc_specific));



        int orig_size = set_copy.size();

        set_copy.remove(fc_specific);
        session.delete(fc_specific);

        int next_size = set_orig.size();
        assertEquals(orig_size, next_size);
        assertEquals(next_size, orig_size);

        assertEquals(set_orig.size(), set_copy.size());


        //Set<?> set_copy = copy_student1.getDecks();

    }
}

