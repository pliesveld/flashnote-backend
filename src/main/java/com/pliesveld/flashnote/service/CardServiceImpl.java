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
    private StatementRepository statementRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private FlashCardRepository flashCardRepository;

    @Autowired
    private DeckRepository deckRepository;

    @Override
    public Question findQuestionById(final int id) {
        return questionRepository.findOne(id);
    }

    @Override
    public Answer findAnswerById(final int id) {
        return answerRepository.findOne(id);
    }

    @Override
    public AbstractStatement findStatementById(final int id) {
        return statementRepository.findOneById(id);
    }

    @Override
    public List<FlashCard> findFlashCardsByContainingQuestionId(final int questionId) throws QuestionNotFoundException {
        if (!questionRepository.exists(questionId))
            throw new QuestionNotFoundException(questionId);

        return flashCardRepository.findAllByQuestion_id(questionId);
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
    public Question createQuestion(final Question question) {
        return questionRepository.save(question);
    }

    @Override
    public Answer createAnswer(final Answer answer) {
        return answerRepository.save(answer);
    }

    @Override
    public void update(final Question question) {
        questionRepository.save(question);

    }

    @Override
    public void deleteAnswer(final int answerId) {
        answerRepository.delete(answerId);
    }

    @Override
    public void deleteQuestion(final int questionId) {
        questionRepository.delete(questionId);
    }

    @Override
    public FlashCard createFlashCard(final Question question, final Answer answer) {

        final FlashCard fc = new FlashCard(question, answer);
        return flashCardRepository.save(fc);
    }

    @Override
    public FlashCard createFlashCardReferecingQuestion(final int questionId, Answer answer) throws QuestionNotFoundException {
        if (!questionRepository.exists(questionId))
            throw new QuestionNotFoundException(questionId);

        if (!answerRepository.exists(answer.getId())) {
            answer = answerRepository.save(answer);
        } else {
            final FlashCard fc = flashCardRepository.findOne(new FlashCardPrimaryKey(questionId, answer.getId()));
            if (fc != null)
                throw new FlashCardCreateException(fc.getId());
        }

        final Question question = questionRepository.findOne(questionId);
        return createFlashCard(question, answer);
    }

}
