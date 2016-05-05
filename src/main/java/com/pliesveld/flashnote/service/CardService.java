package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.exception.QuestionBankNotFoundException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Transactional(readOnly = true)
public interface CardService {
    
    Deck findDeckById(int id) throws DeckNotFoundException;
    
    Question findQuestionById(int id);
    
    Answer findAnswerById(int id);
    
    AbstractStatement findStatementById(int id);

    Long countQuestions();
    
    Long countAnswers();
    
    Long countFlashCards();
    
    Long countDecks();
    
    Long countQuestionBanks();

    boolean doesCategoryIdExist(int id);

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
    void addToDeckFlashCard(Deck deck, FlashCard fc);

    List<Deck> findAllDecks();

    @Transactional
    Deck createDeck(Deck deck);

    @Transactional
    void deleteDeck(int id)                  throws DeckNotFoundException;

    List<QuestionBank> findAllQuestionBanks();

    @Transactional
    QuestionBank createQuestionBank(QuestionBank questionBank);

    QuestionBank findQuestionBankById(int id)   throws QuestionBankNotFoundException;

    @Transactional
    void deleteQuestionBank(int id)             throws QuestionBankNotFoundException;
}
