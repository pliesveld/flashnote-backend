package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.exception.QuestionBankNotFoundException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface CardService {
    @Transactional(readOnly = true)
    Deck findDeckById(int id) throws DeckNotFoundException;
    @Transactional(readOnly = true)
    Question findQuestionById(int id);
    @Transactional(readOnly = true)
    Answer findAnswerById(int id);
    @Transactional(readOnly = true)
    AbstractStatement findStatementById(int id);

    @Transactional(readOnly = true)
    Long countQuestions();
    @Transactional(readOnly = true)
    Long countAnswers();
    @Transactional(readOnly = true)
    Long countFlashCards();
    @Transactional(readOnly = true)
    Long countDecks();

    @Transactional(readOnly = true)
    List<FlashCard> findFlashCardsByContainingQuestionId(int questionId) throws QuestionNotFoundException;

    FlashCard createFlashCard(Question question, Answer answer) throws FlashCardCreateException;
    FlashCard createFlashCardReferecingQuestion(int questionId, Answer answer) throws QuestionNotFoundException, FlashCardCreateException;

    Question createQuestion(Question question);
    Answer createAnswer(Answer answer);

    void addToDeckFlashCard(Deck deck, FlashCard fc);

    @Transactional(readOnly = true)
    List<Deck> findAllDecks();

    Deck createDeck(Deck deck);

    void deleteDeck(int id)                  throws DeckNotFoundException;

    @Transactional(readOnly = true)
    List<QuestionBank> findAllQuestionBanks();

    QuestionBank createQuestionBank(QuestionBank questionBank);

    @Transactional(readOnly = true)
    QuestionBank findQuestionBankById(int id);

    void deleteQuestionBank(int id)             throws QuestionBankNotFoundException;

    @Transactional(readOnly = true)
    boolean doesCategoryIdExist(int id);
}
