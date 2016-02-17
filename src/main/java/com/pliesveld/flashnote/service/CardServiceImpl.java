package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Answer;
import com.pliesveld.flashnote.domain.Deck;
import com.pliesveld.flashnote.domain.FlashCard;
import com.pliesveld.flashnote.domain.FlashCardPrimaryKey;
import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.exception.DeckNotFoundException;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import com.pliesveld.flashnote.exception.ResourceRepositoryException;
import com.pliesveld.flashnote.repository.AnswerRepository;
import com.pliesveld.flashnote.repository.DeckRepository;
import com.pliesveld.flashnote.repository.FlashCardRepository;
import com.pliesveld.flashnote.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.transaction.NotSupportedException;

/**
 * Created by happs on 1/19/16.
 */
@Service(value = "cardService")
public class CardServiceImpl implements CardService {
    
    private static final org.apache.logging.log4j.Logger LOG = org.apache.logging.log4j.LogManager.getLogger();

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
    @Transactional
    public FlashCard createFlashCard(Question question, Answer answer) {
                   
        FlashCard fc = new FlashCard(question,answer);
        flashCardRepository.save(fc);
        return fc;
    }

    @Override
    @Transactional
    public FlashCard createFlashCardReferecingQuestion(int questionId, Answer answer) throws QuestionNotFoundException {
        if(!questionRepository.exists(questionId))
            throw new QuestionNotFoundException(questionId);

        if(!answerRepository.exists(answer.getId()))
        {
            answer.setId(null);
            answerRepository.save(answer);
        } else {
            FlashCard fc = flashCardRepository.findOne(new FlashCardPrimaryKey(questionId, answer.getId()));
            if(fc != null)
                throw new FlashCardCreateException(fc.getId());
        }

        Question question = questionRepository.getOne(questionId);
        return createFlashCard(question,answer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FlashCard> findFlashCardsByContainingQuestionId(int questionId) throws QuestionNotFoundException {
        if(!questionRepository.exists(questionId))
            throw new QuestionNotFoundException(questionId);

        return flashCardRepository.findAllByQuestion(questionId);
    }

    @Override
    @Transactional(readOnly = true)
    public Deck findDeckById(int id) throws DeckNotFoundException {
        Deck deck = deckRepository.findOne(id);
        if(deck == null)
            throw new DeckNotFoundException(id);
        
        return deck;
    }
    
    
}
