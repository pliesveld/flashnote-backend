package com.pliesveld.flashnote.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = com.pliesveld.config.SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class SampleCardTest
{
    @Autowired
    SessionFactory sessionFactory;

    @Before
    public void setup()
    {
        /*
         * ApplicationContext ctx = new
         * AnnotationConfigApplicationContext(SpringConfig.class);
         * LocalSessionFactoryBean sfb = (LocalSessionFactoryBean)
         * ctx.getBean("&sessionFactory"); sessionFactory =
         * sfb.getConfiguration().buildSessionFactory();
         */
    }

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
    public void generateCategoryEmpty()
    {
        Session session = sessionFactory.getCurrentSession();
        assertNotNull(session);
        session.beginTransaction();
        Category category = session.get(Category.class, (short) 1);
        assertNull(category);
         
    }

    @Test
    public void generateCategorySingle()
    {
        Session session = sessionFactory.getCurrentSession();
        assertNotNull(session);


        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("ROOT category description");
        Short new_id = (Short) session.save(category);

        Category category_retrieved = session.get(Category.class, new_id);
        assertNotNull(category_retrieved);

        assertEquals("Retrieved same as persisted", category.getName(), category_retrieved.getName());

         
    }

    @Test
    public void generateCategoryHierarchy()
    {
        Session session = sessionFactory.getCurrentSession();
        assertNotNull(session);


        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("ROOT category description");

        Category chld = new Category();
        category.setName("CHILD");

        category.addChildCategory(chld);
        Short new_id = (Short) session.save(category);

        Category category_retrieved = session.get(Category.class, new_id);
        assertNotNull(category_retrieved);
        Set<?> set_child = category_retrieved.getChildCategories();
        assertNotNull(set_child);
        assertEquals(set_child.size(), 1);

        Category chld_retrieved = (Category) set_child.iterator().next();
        assertEquals("Retrieved same as persisted", category.getName(), category_retrieved.getName());
        assertEquals("Child Retrieved same as persisted", chld.getName(), chld_retrieved.getName());

         
    }

    @Test
    public void generateCategoryTestEmpty()
    {
        Session session = sessionFactory.getCurrentSession();
        assertNotNull(session);
        session.beginTransaction();
        Category category = session.get(Category.class, (short) 1);
        assertNull(category);
         
    }

    @Test 
    public void testQuestion()
    {
        Session session = sessionFactory.getCurrentSession();
        assertNotNull(session);
        session.beginTransaction();
        Question q = new Question();
        q.setContent("This is a question?");
        Integer q_id = (Integer) session.save(q);
        Answer a = new Answer();
        a.setContent("This is an answer.");
        Integer a_id = (Integer) session.save(a);

        FlashCard fc = new FlashCard(q,a);
        session.save(fc);
         
    }

    @Test
    public void testAnswerModifications()
    {
        Session session = sessionFactory.getCurrentSession();
        assertNotNull(session);
        session.beginTransaction();

        final String ANSWER_ORIG = "original answer.";
        final String ANSWER_MODIFIED = "modified answer.";

        assertNotNull(session);
        session.beginTransaction();
        Question q = new Question("This is a question?");
        Question q2 = new Question("This is a question?");

        Serializable q_id = session.save(q);
        Serializable q2_id = session.save(q2);

        Answer a = new Answer();
        a.setContent(ANSWER_ORIG);
        Serializable a_id = session.save(a);

        FlashCard fc = new FlashCard(q,a);
        Serializable fc_id = session.save(fc);

        FlashCard fc2 = new FlashCard(q2,a);
        Serializable fc2_id = session.save(fc2);

        Answer a_loaded = session.load(Answer.class,a_id);
        a_loaded.setContent(ANSWER_MODIFIED);

        assertEquals("Loaded answer does not change content of original reference",ANSWER_MODIFIED,a.getContent());
        assertEquals("Referenced flashcard answer does not change original",ANSWER_MODIFIED,fc.getAnswer().getContent());

        //session.update(a_loaded);

        assertEquals("After answer updated, original reference to flashcard does not match modified",ANSWER_MODIFIED,fc.getAnswer().getContent());
        assertEquals("After answer updated, original reference to flashcard does not match modified",ANSWER_MODIFIED,fc2.getAnswer().getContent());

        FlashCard fc3 = session.load(FlashCard.class,fc2_id);

        assertEquals("After answer updated, loaded flashcard does not match modified",ANSWER_MODIFIED,fc3.getAnswer().getContent());

         
    }



    @Test
    public void testQuestionCascade()
    {
        Session session = sessionFactory.getCurrentSession();
        assertNotNull(session);

        Question q = new Question();
        q.setContent("This is a question?");
//        Integer q_id = (Integer) session.save(q);
        Answer a = new Answer();
        a.setContent("This is an answer.");
//        Integer a_id = (Integer) session.save(a);

        FlashCard fc = new FlashCard(q,a);
        session.save(fc);
         
    }

    @Test
    public void createCardCascadeAll()
    {
        Session session = sessionFactory.getCurrentSession();



        FlashCard fc = new FlashCard(new Question("q"),new Answer("a"));
        session.save(fc);
         
    }
}
