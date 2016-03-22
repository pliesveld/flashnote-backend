package com.pliesveld.flashnote.unit.domain.entity;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.unit.spring.DefaultEntityTestAnnotations;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultEntityTestAnnotations
@Transactional
public class QuestionBankFlashCardDeleteTest extends AbstractDomainEntityUnitTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @PersistenceContext
    EntityManager entityManager;

    Integer que_id;
    Integer ans_id;
    FlashCardPrimaryKey fc_id;
    Integer cat_id;
    Integer qb_id;


    @Before
    public void setupEntities()
    {
        FlashCard flashCard = flashcardBean();
        entityManager.persist(flashCard);

        fc_id = flashCard.getId();
        que_id = fc_id.getQuestionId();
        ans_id = fc_id.getAnswerId();

        Category category = this.categoryBean();
        category = this.categoryRepository.save(category);
        cat_id = category.getId();

        QuestionBank bank = this.questionBankBean();
        bank.add(flashCard.getQuestion());
        bank.setCategory(category);
        bank = this.questionBankRepository.save(bank);
        qb_id = bank.getId();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void testEntitySanity()
    {
        this.assertQuestionRepositoryCount(1);
        this.assertAnswerRepositoryCount(1);
        this.assertQuestionBankRepositoryCount(1);


        Question que = questionRepository.findOne(que_id);
        assertNotNull(que);
        assertTrue(questionBankRepository.findOne(qb_id).getQuestionCollection().contains(que));
        FlashCard fc = flashCardRepository.findOne(fc_id);
        assertNotNull(fc);
        assertTrue(fc.getQuestion().equals(que));
    }

    @After
    public void flushAfter()
    {
        entityManager.flush();
    }


    @Test
    public void testQuestionRemovalFKViolation() {
        thrown.expect(PersistenceException.class);
        questionRepository.deleteAll();
    }

    @Test
    public void testAnswerRemovalFKViolation() {
        thrown.expect(PersistenceException.class);
        answerRepository.deleteAll();
    }

    @Test
    public void testAnswerRemoval()
    {
        flashCardRepository.deleteAll();
        answerRepository.deleteAll();
    }

    @Test
    public void testFlashcardQuestionRemovalFKViolation()
    {
        thrown.expect(PersistenceException.class);
        flashCardRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    public void testFlashCardRemoval() {
        flashCardRepository.deleteAll();
    }

    @Test
    public void testCategoryRemovalFKViolation() {
        thrown.expect(PersistenceException.class);
        categoryRepository.deleteAll();
    }

    @Test
    public void testCategory() {
        questionBankRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void testRemoveRepoQuestionBank() {
        questionBankRepository.deleteAll();
    }




}
