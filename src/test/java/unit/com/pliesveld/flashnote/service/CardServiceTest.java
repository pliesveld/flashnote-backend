package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.repository.AnswerRepository;
import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.flashnote.repository.StatementRepository;
import com.pliesveld.flashnote.spring.DefaultServiceTestAnnotations;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@DefaultServiceTestAnnotations
@Transactional
public class CardServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();


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
    
    @Test
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