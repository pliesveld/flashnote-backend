package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.AbstractStatement;
import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Transactional(readOnly = true)
public interface CardService {

    Question findQuestionById(int id);
    
    Answer findAnswerById(int id);
    
    AbstractStatement findStatementById(int id);

    Long countQuestions();
    
    Long countAnswers();
    
    Long countFlashCards();
    
    Long countDecks();
    
    Long countQuestionBanks();

    List<FlashCard> findFlashCardsByContainingQuestionId(int questionId) throws QuestionNotFoundException;

    @Transactional
    FlashCard createFlashCard(Question question, Answer answer) throws FlashCardCreateException;

    @Transactional
    FlashCard createFlashCardReferecingQuestion(int questionId, Answer answer) throws QuestionNotFoundException, FlashCardCreateException;

    @Transactional
    Question createQuestion(Question question);

    @Transactional
    Answer createAnswer(Answer answer);

    @Transactional
    void update(Question question);
}
