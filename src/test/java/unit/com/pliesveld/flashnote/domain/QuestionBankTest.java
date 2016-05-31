package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.spring.BlankEntityTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceUnitUtil;
import java.io.Serializable;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@BlankEntityTestAnnotations
@Transactional
final public class QuestionBankTest extends AbstractTransactionalDomainEntityUnitTest
{
    private static final Logger LOG = LogManager.getLogger();

    private int cat_id;

    @Before
    public void givenCategory()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("ROOT category description");
        entityManager.persist(category);
        entityManager.flush();
        cat_id = category.getId();
    }

    @Test
    public void givenCategoryInPersistenceContext_whenFindCategory_whenPersistBank_thenCorrect()
    {
        QuestionBank questionBank = new QuestionBank();
        Category category = entityManager.find(Category.class,cat_id);
        assertNotNull(category);
        questionBank.setCategory(category);
        questionBank.setDescription("A sample question bank with no questions.");
        entityManager.persist(questionBank);
    }

    @Test
    public void givenCategoryInPersistenceContext_whenCategoryByReference_whenPersistBank_thenCorrect()
    {
        QuestionBank questionBank = new QuestionBank();
        Category category = entityManager.getReference(Category.class, cat_id);
        assertNotNull(category);
        questionBank.setCategory(category);
        questionBank.setDescription("A sample question bank with no questions.");
        entityManager.persist(questionBank);
    }

    @Test
    public void givenCategory_whenFindCategory_whenPersistBank_thenCorrect()
    {
        entityManager.clear();
        QuestionBank questionBank = new QuestionBank();
        Category category = entityManager.find(Category.class, cat_id);
        assertNotNull(category);
        questionBank.setCategory(category);
        questionBank.setDescription("A sample question bank with no questions.");
        entityManager.persist(questionBank);
    }

    @Test
    public void givenCategory_whenDetachedCategory_whenPersistBank_thenCorrect()
    {
        entityManager.clear();
        QuestionBank questionBank = new QuestionBank();
        Category category = new Category();
        category.setId(cat_id);

        assertEntityHasState(category, EntityState.DETACHED);

        questionBank.setCategory(category);
        questionBank.setDescription("A sample question bank with no questions.");
        entityManager.persist(questionBank);

        assertEntityHasState(questionBank, EntityState.PERSISTENT);

    }

    @Test
    public void givenCategory_whenDetachedCategory_whenMergeBank_thenCorrect()
    {
        entityManager.clear();
        QuestionBank questionBank = new QuestionBank();
        Category category = new Category();
        category.setId(cat_id);

        assertEntityHasState(category, EntityState.DETACHED);
        questionBank.setCategory(category);
        questionBank.setDescription("A sample question bank with no questions.");
        QuestionBank questionBank2 = entityManager.merge(questionBank);

        /*
            The object passed into merge() does not become managed by the persistence context
         */
        assertEntityHasState(questionBank, EntityState.TRANSIENT);

        /*
            The object returned from merge() does become managed by the persistence context
         */
        assertEntityHasState(questionBank2, EntityState.PERSISTENT);

    }


    @Test
    public void givenPersistingBank_whenQuestionCascade_thenCorrect()
    {
        QuestionBank questionBank = new QuestionBank();
        Category category = entityManager.getReference(Category.class, cat_id);
        assertNotNull(category);
        questionBank.setCategory(category);
        questionBank.setDescription("A sample question bank with no questions.");

        Question question = new Question("Que?");

        entityManager.persist(question);
        assertEntityHasState(question, EntityState.PERSISTENT);

        questionBank.add(question);

        entityManager.persist(questionBank);
    }

    @Test
    public void givenPersistingBank_whenQuestionByReference_thenCorrect()
    {
        Integer question_id;
        {
            Question question = new Question("Que?");
            entityManager.persist(question);
            entityManager.flush();
            question_id = question.getId();
            entityManager.clear();
        }
        assertNotNull(question_id);

        QuestionBank questionBank = new QuestionBank();
        Category category = entityManager.getReference(Category.class, cat_id);
        assertNotNull(category);
        questionBank.setCategory(category);
        questionBank.setDescription("A sample question bank with no questions.");
        entityManager.persist(questionBank);

        Question question = entityManager.getReference(Question.class, question_id);
        questionBank.add(question);
    }


    @Test
    public void givenPersistingBank_whenQuestionDetached_thenCorrect()
    {
        Integer question_id;
        {
            Question question = new Question("Que?");
            entityManager.persist(question);
            entityManager.flush();
            question_id = question.getId();
            entityManager.clear();
        }
        assertNotNull(question_id);

        QuestionBank questionBank = new QuestionBank();
        Category category = entityManager.getReference(Category.class, cat_id);
        assertNotNull(category);
        questionBank.setCategory(category);
        questionBank.setDescription("A sample question bank with no questions.");
        entityManager.persist(questionBank);

        Question question = new Question();
        question.setId(question_id);

        questionBank.add(question);
    }

    @Test
    public void givenQuestionBank_whenProxyReference_thenCollectionContentsNotLoaded()
    {
        QuestionBank questionBank = new QuestionBank();
        Category category = entityManager.find(Category.class,cat_id);
        questionBank.setCategory(category);
        questionBank.setDescription("A sample question bank with a question.");

        for(int i = 0; i < 3;i++)
        {
            Question question = this.questionBean();

            entityManager.persist(question);
            entityManager.flush();
            questionBank.add(question);
        }

        entityManager.persist(questionBank);
        Serializable qb_id = questionBank.getId();
        entityManager.flush();
        entityManager.clear();

        questionBank = entityManager.getReference(QuestionBank.class, qb_id);
        assertNotNull(questionBank);

//        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();
        PersistenceUnitUtil persistenceUtil = entityManager.getEntityManagerFactory().getPersistenceUnitUtil();

        assertFalse("QuestionBank proxy should be proxy", Hibernate.isInitialized(questionBank));
        assertFalse("All FetchType.EAGER have been loaded", persistenceUtil.isLoaded(questionBank));
        assertFalse("identifier attribute has been loaded", persistenceUtil.isLoaded(questionBank, "id"));
        assertFalse("Collection has been loaded", persistenceUtil.isLoaded(questionBank,"questions"));

        Collection<Question> questionCollectionProxy = questionBank.getQuestions();
        assertFalse(Hibernate.isInitialized(questionCollectionProxy));


        assertEquals(3, questionCollectionProxy.size());

        assertFalse("Getting size of proxy collection should not initialize the collection", Hibernate.isInitialized(questionCollectionProxy));

        assertFalse(persistenceUtil.isLoaded(questionBank, "questions"));

//        Hibernate.initialize(questionCollectionProxy);
//        disableSQL();
//
//        assertTrue(Hibernate.isInitialized(questionCollectionProxy));
//
//        assertTrue(persistenceUtil.isLoaded(questionBank, "questions"));


        /*
            Iterate through collection, reading only the id field.  Ensure that the content attribute of the
            question has not been loaded.
         */

        questionCollectionProxy.forEach(
                (q) ->
                {
                    //q.getId();
                    LOG.error("content loaded? {}", persistenceUtil.isLoaded(q, "content"));
                }

        );
    }



}
