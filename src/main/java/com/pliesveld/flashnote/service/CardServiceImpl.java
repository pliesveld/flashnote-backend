package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.*;
import com.pliesveld.flashnote.exception.FlashCardCreateException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import com.pliesveld.flashnote.repository.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(value = "cardService")
public class CardServiceImpl implements CardService {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    StatementRepository statementRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuestionBankRepository questionBankRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    FlashCardRepository flashCardRepository;

    @Autowired
    DeckRepository deckRepository;

    @Override
    public Question findQuestionById(int id) {
        return questionRepository.findOne(id);
    }

    @Override
    public Answer findAnswerById(int id) {
        return answerRepository.findOne(id);
    }

    @Override
    public AbstractStatement findStatementById(int id) {
        return statementRepository.findOneById(id);
    }

    @Override
    public List<FlashCard> findFlashCardsByContainingQuestionId(int questionId) throws QuestionNotFoundException {
        if(!questionRepository.exists(questionId))
            throw new QuestionNotFoundException(questionId);

        return flashCardRepository.findAllByQuestion(questionId);
    }

    @Override
    public Long countQuestions() {
        return questionRepository.count();
    }

    @Override
    public Long countAnswers() {
        return answerRepository.count();
    }

    @Override
    public Long countFlashCards() {
        return flashCardRepository.count();
    }

    @Override
    public Long countDecks() {
        return deckRepository.count();
    }

    @Override
    public Long countQuestionBanks() {
        return questionBankRepository.count();
    }

    @Override
    public Question createQuestion(Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Answer createAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    @Override
    public void update(Question question) {
        questionRepository.save(question);

    }

    @Override
    public FlashCard createFlashCard(Question question, Answer answer) {
                   
        FlashCard fc = new FlashCard(question,answer);
        flashCardRepository.save(fc);
        return fc;
    }

    @Override
    public FlashCard createFlashCardReferecingQuestion(int questionId, Answer answer) throws QuestionNotFoundException {
        if(!questionRepository.exists(questionId))
            throw new QuestionNotFoundException(questionId);

        if(!answerRepository.exists(answer.getId()))
        {
            answer = answerRepository.save(answer);
        } else {
            FlashCard fc = flashCardRepository.findOne(new FlashCardPrimaryKey(questionId, answer.getId()));
            if(fc != null)
                throw new FlashCardCreateException(fc.getId());
        }

        Question question = questionRepository.findOne(questionId);
        return createFlashCard(question,answer);
    }

}
