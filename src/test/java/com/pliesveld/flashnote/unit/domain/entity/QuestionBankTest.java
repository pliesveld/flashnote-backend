package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.Category;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.unit.spring.DefaultTestAnnotations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultTestAnnotations
@Transactional
public class QuestionBankTest
{
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

        Question question = new Question();
        question.setTitle("A sample question");
        question.setContent("What is the meaning of life?");
        entityManager.persist(question);
        entityManager.flush();

        questionBank.add(question);

        entityManager.persist(questionBank);
        entityManager.flush();
    }



}
