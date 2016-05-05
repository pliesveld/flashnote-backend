package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.spring.DefaultEntityTestAnnotations;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUtil;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class QuestionBankTest extends AbstractDomainEntityUnitTest
{
    private static final Logger LOG = LogManager.getLogger();

    @PersistenceContext
    EntityManager entityManager;

    Serializable cat_id;

    @Before
    public void generateCategorySingle()
    {
        Category category = new Category();
        category.setName("ROOT");
        category.setDescription("ROOT category description");
        entityManager.persist(category);
        entityManager.flush();
        cat_id = category.getId();
    }

    @Test
    public void questionBankCreation()
    {
        QuestionBank questionBank = new QuestionBank();
        Category category = entityManager.find(Category.class,cat_id);
        assertNotNull(category);
        questionBank.setCategory(category);
        questionBank.setDescription("A sample question bank with no questions.");
        entityManager.persist(questionBank);
        entityManager.flush();
    }

    @Test
    public void questionBankWithQuestions()
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

        questionBank = entityManager.find(QuestionBank.class, qb_id);
        assertNotNull(questionBank);

        PersistenceUtil persistenceUtil = Persistence.getPersistenceUtil();

        LOG.info(Hibernate.isInitialized(questionBank));
        LOG.info(persistenceUtil.isLoaded(questionBank));
        LOG.info(persistenceUtil.isLoaded(questionBank,"id"));
        LOG.info(persistenceUtil.isLoaded(questionBank,"questions"));

        enableSQL();
        assertEquals(questionBank.getQuestions().size(),3);
        LOG.info(persistenceUtil.isLoaded(questionBank,"questions"));

        questionBank.getQuestions().forEach(
                (q) ->
                {
                    LOG.info(persistenceUtil.isLoaded(q,"id"));
                }

        );
        LOG.info(persistenceUtil.isLoaded(questionBank,"questions"));
        disableSQL();


    }



}
