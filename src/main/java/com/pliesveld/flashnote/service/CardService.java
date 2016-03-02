package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.Question;
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

}
