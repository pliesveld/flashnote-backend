package com.pliesveld.flashnote.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = com.pliesveld.config.SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
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
        Session session = sessionFactory.openSession();
        assertNotNull(session);

        assertTrue(session.isOpen());
        assertTrue(session.isConnected());
        session.close();
    }

    @Test
    public void generateCategoryEmpty()
    {
        Session session = sessionFactory.openSession();
        assertNotNull(session);
        session.beginTransaction();
        Category category = session.get(Category.class, (short) 1);
        assertNull(category);
        session.getTransaction().rollback();
    }

    @Test
    public void generateCategorySingle()
    {
        Session session = sessionFactory.openSession();
        assertNotNull(session);
        session.beginTransaction();

        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("ROOT category description");
        Short new_id = (Short) session.save(category);

        Category category_retrieved = session.get(Category.class, new_id);
        assertNotNull(category_retrieved);

        assertEquals("Retrieved same as persisted", category.getName(), category_retrieved.getName());

        session.getTransaction().rollback();
    }

    @Test
    public void generateCategoryHierarchy()
    {
        Session session = sessionFactory.openSession();
        assertNotNull(session);
        session.beginTransaction();

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

        session.getTransaction().rollback();
    }

    @Test
    public void generateCategoryTestEmpty()
    {
        Session session = sessionFactory.openSession();
        assertNotNull(session);
        session.beginTransaction();
        Category category = session.get(Category.class, (short) 1);
        assertNull(category);
        session.getTransaction().rollback();
    }

    @Test 
    public void testQuestion()
    {
        Session session = sessionFactory.openSession();
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
        session.getTransaction().rollback();
    }

    @Test
    public void testQuestionCascade()
    {
        Session session = sessionFactory.openSession();
        assertNotNull(session);
        session.beginTransaction();
        Question q = new Question();
        q.setContent("This is a question?");
//        Integer q_id = (Integer) session.save(q);
        Answer a = new Answer();
        a.setContent("This is an answer.");
//        Integer a_id = (Integer) session.save(a);

        FlashCard fc = new FlashCard(q,a);
        session.save(fc);
        session.getTransaction().rollback();
    }

    @Test
    public void createCardCascadeAll()
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();


        FlashCard fc = new FlashCard(new Question("q"),new Answer("a"));
        session.save(fc);
        session.getTransaction().rollback();
    }
}
