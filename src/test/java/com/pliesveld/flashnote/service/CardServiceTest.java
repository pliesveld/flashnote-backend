package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import com.pliesveld.flashnote.exception.StudentCreateException;
import com.pliesveld.flashnote.repository.AnswerRepository;
import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.spring.SpringTestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringTestConfig.class, loader = AnnotationConfigContextLoader.class)
@Transactional
public class CardServiceTest {

    @Autowired
    CardService cardService;
    
    @Autowired
    QuestionRepository questionRepository;
    
    @Autowired
    AnswerRepository answerRepository;

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
    


}
