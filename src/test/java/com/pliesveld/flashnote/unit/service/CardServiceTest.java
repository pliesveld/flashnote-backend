package com.pliesveld.flashnote.unit.service;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.repository.AnswerRepository;
import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.flashnote.repository.StatementRepository;
import com.pliesveld.flashnote.service.CardService;
import com.pliesveld.flashnote.unit.spring.DefaultServiceTestAnnotations;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultServiceTestAnnotations
@Transactional
public class CardServiceTest {

    @Autowired
    CardService cardService;
    
    @Autowired
    QuestionRepository questionRepository;
    
    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    StatementRepository statementRepository;

    @Test
    public void flashcardCreateSimple()
    {
        Question que = new Question("que?");
        Answer ans = new Answer("Ans.");
        cardService.createFlashCard(que, ans);
    }
    
    @Test
    public void accoutCreationDuplicate()
    {
        Question que = new Question("que?");
        Answer ans = new Answer("Ans.");

        cardService.createFlashCard(que, ans);
        cardService.createFlashCard(que, ans);
    }
    
    @Test(expected = FlashCardCreateException.class)
    public void accoutCreationDuplicateByReference()
    {
        Question que = new Question("que?");
        Answer ans = new Answer("Ans.");
        
        questionRepository.save(que);
        answerRepository.save(ans);
        
        cardService.createFlashCardReferecingQuestion(que.getId(), ans);
        cardService.createFlashCardReferecingQuestion(que.getId(), ans);
    }

    @Test
    public void findByEmail()
    {
        Question que = new Question("que?");
        Answer ans = new Answer("Ans.");

        questionRepository.save(que);
        answerRepository.save(ans);

        assertEquals(1, questionRepository.findAllByAuthor("SYSTEM").count());
        assertEquals(1, answerRepository.findAllByAuthor("SYSTEM").count());
        assertEquals(2, statementRepository.findAllByAuthor("SYSTEM").count());
    }



}
