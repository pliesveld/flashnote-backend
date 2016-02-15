package com.pliesveld.flashnote.domain;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
@ContextConfiguration(classes = com.pliesveld.spring.SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class SampleCardTest
{
    @PersistenceContext
    EntityManager entityManager;

    @Before
    public void setup()
    {
        /*
         * ApplicationContext ctx = new
         * AnnotationConfigApplicationContext(SpringConfig.class);
         * LocalEntityManagerFactoryBean sfb = (LocalEntityManagerFactoryBean)
         * ctx.getBean("&entityManager"); entityManager =
         * sfb.getConfiguration().buildEntityManagerFactory();
         */
    }

    @Test
    public void entityManagerWired()
    {
        assertNotNull(entityManager);
    }


    @Test
    public void generateCategoryEmpty()
    {
        Category category = entityManager.find(Category.class, 1);
        assertNull(category);
         
    }

    @Test
    public void generateCategorySingle()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("ROOT category description");
        entityManager.persist(category);
        entityManager.flush();
        Serializable new_id = category.getId();

        Category category_retrieved = entityManager.find(Category.class, new_id);
        assertNotNull(category_retrieved);
        assertEquals("Retrieved same as persisted", category.getName(), category_retrieved.getName());
    }

    @Test
    public void generateCategoryHierarchy()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("ROOT category description");

        Category chld = new Category();
        category.setName("CHILD");

        category.addChildCategory(chld);
        entityManager.persist(category);
        entityManager.flush();
        Serializable new_id = category.getId();

        Category category_retrieved = entityManager.find(Category.class, new_id);
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
        Category category = entityManager.find(Category.class, 1);
        assertNull(category);
         
    }

    @Test 
    public void testQuestion()
    {
        Question q = new Question();
        q.setContent("This is a question?");
        entityManager.persist(q);
        entityManager.flush();
        Integer q_id = q.getId();

        Answer a = new Answer();
        a.setContent("This is an answer.");

        entityManager.persist(a);
        entityManager.flush();
        Integer a_id = a.getId();

        FlashCard fc = new FlashCard(q,a);
        entityManager.persist(fc);
         
    }

    @Test
    public void testAnswerModifications()
    {
        final String ANSWER_ORIG = "original answer.";
        final String ANSWER_MODIFIED = "modified answer.";


        Question q = new Question("This is a question?");
        Question q2 = new Question("This is a question?");

        entityManager.persist(q);
        entityManager.persist(q2);
        entityManager.flush();

        Serializable q_id = q.getId();
        Serializable q2_id = q2.getId();

        Answer a = new Answer();
        a.setContent(ANSWER_ORIG);
        entityManager.persist(a);
        entityManager.flush();
        Serializable a_id = a.getId();

        FlashCard fc = new FlashCard(q,a);
        entityManager.persist(fc);
        entityManager.flush();
        Serializable fc_id = fc.getId();

        FlashCard fc2 = new FlashCard(q2,a);
        entityManager.persist(fc2);
        entityManager.flush();
        Serializable fc2_id = fc2.getId();

        Answer a_loaded = entityManager.find(Answer.class,a_id);
        a_loaded.setContent(ANSWER_MODIFIED);

        assertEquals("Loaded answer does not change content of original reference",ANSWER_MODIFIED,a.getContent());
        assertEquals("Referenced flashcard answer does not change original",ANSWER_MODIFIED,fc.getAnswer().getContent());

        //session.update(a_loaded);

        assertEquals("After answer updated, original reference to flashcard does not match modified",ANSWER_MODIFIED,fc.getAnswer().getContent());
        assertEquals("After answer updated, original reference to flashcard does not match modified",ANSWER_MODIFIED,fc2.getAnswer().getContent());

        FlashCard fc3 = entityManager.find(FlashCard.class,fc2_id);

        assertEquals("After answer updated, loaded flashcard does not match modified",ANSWER_MODIFIED,fc3.getAnswer().getContent());

         
    }



    @Test
    public void testQuestionCascade()
    {
        Question q = new Question();
        q.setContent("This is a question?");
//        Integer q_id = (Integer) entityManager.persist(q);
        Answer a = new Answer();
        a.setContent("This is an answer.");
//        Integer a_id = (Integer) entityManager.persist(a);

        FlashCard fc = new FlashCard(q,a);
        entityManager.persist(fc);
        entityManager.flush();
    }

    @Test
    public void createCardCascadeAll()
    {
        FlashCard fc = new FlashCard(new Question("q"),new Answer("a"));
        entityManager.persist(fc);
        entityManager.flush();
    }
}
