package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.Question;

import javax.transaction.NotSupportedException;

public interface CardService {
    FlashCard create(Question question,Answer answer) throws NotSupportedException;

    Long countQuestions();
    Long countAnswers();
    Long countFlashCards();
    Long countDecks();

    Iterable<Deck> findDeckBy(int id) throws NotSupportedException;


}
