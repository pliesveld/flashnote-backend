package com.pliesveld.flashnote.model;

import com.pliesveld.config.SpringTestConfig;
import org.hibernate.*;
import org.hibernate.criterion.Projections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
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
        Session session = sessionFactory.openSession();
        assertNotNull(session);

        assertTrue(session.isOpen());
        assertTrue(session.isConnected());
        session.close();
    }

    @Test
    public void verifyEmpty()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction().begin();
        {
            Query query = session.createQuery("SELECT count(*) FROM com.pliesveld.flashnote.model.Deck");
            Criteria crit = session.createCriteria(Deck.class);
            crit.setProjection(Projections.rowCount());

            Long count = (Long)query.uniqueResult();
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

        session.getTransaction().commit();
    }


    private void clearDeck()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction().begin();
        SQLQuery sql;

        sql = session.createSQLQuery("DELETE FROM DECK_FLASHCARD;");
        sql.executeUpdate();

        sql = session.createSQLQuery("DELETE FROM DECK;");
        sql.executeUpdate();

        sql = session.createSQLQuery("DELETE FROM FLASHCARD;");
        sql.executeUpdate();

        sql = session.createSQLQuery("DELETE FROM QUESTION;");
        sql.executeUpdate();
        sql = session.createSQLQuery("DELETE FROM ANSWER;");
        sql.executeUpdate();

        session.getTransaction().commit();
    }

    @Test
    public void createStudentDeck()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Student student = new Student();
        student.setName("Student");

        Deck deck = new Deck();
        deck.getFlashCards().add(new FlashCard(new Question("q"),new Answer("a")));
        session.save(deck);

        //student.addDeck(deck);

        session.getTransaction().rollback();
    }

    @Test
    public void createStudentDeckCascade()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Student student = new Student();
        student.setName("Student");

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


        session.getTransaction().rollback();
    }


    @Test
    public void studentIdentityTest()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Student student1 = new Student("student");
        Student student2 = new Student("student");

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
        assertTrue(student1.getName() == student2.getName());

        assertTrue(copy_student1.getName().equals(student1.getName()));

        student1.setName("otherstudent");       // property has shared reference
        assertTrue(copy_student1.getName().equals("otherstudent"));

        assertFalse(student1.getName().equals(student2.getName()));
        assertFalse(student1.getName() == student2.getName());

        assertTrue(student1.equals(copy_student1));
        assertTrue(copy_student1.equals(student1));

        assertTrue(copy_student1.getName().equals(student1.getName()));

        session.getTransaction().rollback();
    }

    }

