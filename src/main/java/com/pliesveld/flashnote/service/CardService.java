package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;

import java.util.List;

public interface CardService {

    Long countQuestions();
    Long countAnswers();
    Long countFlashCards();
    Long countDecks();

    List<FlashCard> findFlashCardsByContainingQuestionId(int questionId) throws QuestionNotFoundException;

    FlashCard createFlashCard(Question question,Answer answer) throws FlashCardCreateException;
    FlashCard createFlashCardReferecingQuestion(int questionId,Answer answer) throws QuestionNotFoundException, FlashCardCreateException;

    Deck findDeckById(int id) throws DeckNotFoundException;

    Question createQuestion(Question question);
    Answer createAnswer(Answer answer);

    Question findQuestionById(int id);
    Answer findAnswerById(int id);

    AbstractStatement findStatementById(int id);
}
