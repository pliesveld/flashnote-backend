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

    Question findQuestionById(final int id);

    Answer findAnswerById(final int id);

    AbstractStatement findStatementById(final int id);

    Long countQuestions();

    Long countAnswers();

    Long countFlashCards();

    Long countDecks();

    Long countQuestionBanks();

    List<FlashCard> findFlashCardsByContainingQuestionId(final int questionId) throws QuestionNotFoundException;

    @Transactional
    FlashCard createFlashCard(final Question question, final Answer answer) throws FlashCardCreateException;

    @Transactional
    FlashCard createFlashCardReferecingQuestion(final int questionId, final Answer answer) throws QuestionNotFoundException, FlashCardCreateException;

    @Transactional
    Question createQuestion(final Question question);

    @Transactional
    Answer createAnswer(final Answer answer);

    @Transactional
    void update(final Question question);

    @Transactional
    void deleteAnswer(final int answerId);

    @Transactional
    void deleteQuestion(int questionId);
}
