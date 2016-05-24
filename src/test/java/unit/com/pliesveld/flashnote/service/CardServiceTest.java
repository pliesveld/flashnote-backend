package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.spring.DefaultServiceTestAnnotations;
import com.pliesveld.tests.AbstractRepositoryUnitTest;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultServiceTestAnnotations
public class CardServiceTest extends AbstractTransactionalServiceUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Autowired
    CardService cardService;

    @Test
    @DirtiesContext
    public void whenCreatingFlashCard_thenCorrect()
    {
        Question que = new Question("que?");
        Answer ans = new Answer("Ans.");
        cardService.createFlashCard(que, ans);
        assertEquals(flashCardRepository.count(), 1);
    }
    
    @Test
    @DirtiesContext
    public void accoutCreationDuplicate()
    {
        Question que = new Question("que?");
        Answer ans = new Answer("Ans.");

        cardService.createFlashCard(que, ans);
        cardService.createFlashCard(que, ans);
        assertEquals(2, flashCardRepository.count());
    }
    
    @Test
    @DirtiesContext
    public void whenCreatingFlashCardFromPersistedStatements()
    {
        Question que = new Question("que?");
        Answer ans = new Answer("Ans.");

        questionRepository.save(que);
        answerRepository.save(ans);

        cardService.createFlashCard(que, ans);
        cardService.createFlashCard(que, ans);

        assertEquals(1, flashCardRepository.count());
    }


    @Test
    @DirtiesContext
    public void accoutCreationDuplicateByReference()
    {
        Question que = new Question("que?");
        Answer ans = new Answer("Ans.");

        questionRepository.save(que);
        answerRepository.save(ans);

        cardService.createFlashCardReferecingQuestion(que.getId(), ans);
        thrown.expect(FlashCardCreateException.class);
        cardService.createFlashCardReferecingQuestion(que.getId(), ans);
    }




}
