package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.repository.AnswerRepository;
import com.pliesveld.flashnote.repository.DeckRepository;
import com.pliesveld.flashnote.repository.FlashCardRepository;
import com.pliesveld.flashnote.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.NotSupportedException;

/**
 * Created by happs on 1/19/16.
 */
@Service(value = "cardService")
public class CardServiceImpl implements CardService {

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    FlashCardRepository flashCardRepository;

    @Autowired
    DeckRepository deckRepository;

    @Override
    @Transactional(readOnly = true)
    public Long countQuestions() {
        return questionRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countAnswers() {
        return answerRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countFlashCards() {
        return flashCardRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countDecks() {
        return deckRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public FlashCard create(Question question, Answer answer) throws NotSupportedException {
        throw new NotSupportedException();
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Deck> findDeckBy(int id) throws NotSupportedException {
        throw new NotSupportedException();
    }
}
