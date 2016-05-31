package com.pliesveld.flashnote.domain;

import com.pliesveld.flashnote.spring.BlankEntityTestAnnotations;
import org.hibernate.Hibernate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@BlankEntityTestAnnotations
@Transactional
final public class QuestionBankFlashCardDeleteTest extends AbstractTransactionalDomainEntityUnitTest
{
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    Integer que_id;
    Integer ans_id;
    FlashCardPrimaryKey fc_id;
    Integer cat_id;
    Integer qb_id;


    @Before
    public void givenQuestionBank_givenFlashCard_givenSharedEntityQuestion_givenAnswer()
    {
        FlashCard flashCard = flashcardBean();
        entityManager.persist(flashCard);
        assertEntityHasState(flashCard, EntityState.PERSISTENT);


        fc_id = flashCard.getId();
        que_id = fc_id.getQuestionId();
        ans_id = fc_id.getAnswerId();

        assertEntityHasState(flashCard.getQuestion(),EntityState.PERSISTENT);
        assertEntityHasState(flashCard.getAnswer(),EntityState.PERSISTENT);

        Category category = this.categoryBean();
        category = this.categoryRepository.save(category);
        cat_id = category.getId();

        QuestionBank bank = this.questionBankBean();
        bank.add(flashCard.getQuestion());
        bank.setCategory(category);
        bank = this.questionBankRepository.save(bank);

        assertEntityHasState(bank,EntityState.PERSISTENT);

        qb_id = bank.getId();

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    public void whenContext_thenCorrect()
    {
        assertNotNull(fc_id);
        assertNotNull(que_id);
        assertNotNull(ans_id);
        assertNotNull(qb_id);
    }

    @Test
    public void whenEntityCount_thenCorrect()
    {
        this.assertQuestionRepositoryCount(1);
        this.assertAnswerRepositoryCount(1);
        this.assertQuestionBankRepositoryCount(1);
    }

    @Test
    public void whenEntityReferenceLoad_thenCorrect()
    {
        Question que = questionRepository.findOne(que_id);
        assertNotNull(que);
        assertTrue(questionBankRepository.findOne(qb_id).getQuestions().contains(que));
        FlashCard fc = flashCardRepository.findOne(fc_id);
        assertNotNull(fc);
        assertTrue(fc.getQuestion().equals(que));
    }

    @Test
    public void whenQuestionRemoveAll_thenFKViolation() {
        thrown.expect(PersistenceException.class);
        questionRepository.deleteAll();
    }

    @Test
    public void whenAnswerRemoveAll_thenFKViolation() {
        thrown.expect(PersistenceException.class);
        answerRepository.deleteAll();
    }

    @Test
    public void givenFlashCardRemoveAll_whenAnswerRemoveAll_thenCorrect()
    {
        flashCardRepository.deleteAll();
        answerRepository.deleteAll();
    }

    @Test
    public void givenFlashCardRemoveAll_whenQuestionRemoveAll_thenFKViolation()
    {
        thrown.expect(PersistenceException.class);
        flashCardRepository.deleteAll();
        questionRepository.deleteAll();
    }

    @Test
    public void whenFlashCardRemoveAll_thenCorrect() {
        flashCardRepository.deleteAll();
    }

    @Test
    public void whenCategoryRemoveAll_thenFKViolation() {
        thrown.expect(PersistenceException.class);
        categoryRepository.deleteAll();
    }

    @Test
    public void givenBankRemoveAll_whenCategoryRemoveAll_thenCorrect() {
        questionBankRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void whenBankRemoveAll_thenCorrect() {
        questionBankRepository.deleteAll();
    }


    @Test
    public void whenFlashCardRemoveByReference_thenCorrect() {

        FlashCard fc = flashCardRepository.getOne(fc_id);
        assertFalse(Hibernate.isInitialized(fc));
        entityManager.remove(fc);

    }
}
