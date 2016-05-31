package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.exception.QuestionBankNotFoundException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import com.pliesveld.flashnote.repository.QuestionBankRepository;
import com.pliesveld.flashnote.repository.QuestionRepository;
import com.pliesveld.flashnote.repository.specifications.QuestionBankSpecification;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@Service("bankService")
public class BankServiceImpl implements BankService {

    @Autowired
    private QuestionBankRepository questionBankRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<QuestionBank> findAllQuestionBanks() {
        ArrayList<QuestionBank> list = new ArrayList<>();
        questionBankRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public QuestionBank findQuestionBankById(final int id) {
        QuestionBank questionBank = questionBankRepository.findOne(id);
        if (questionBank == null) {
            throw new QuestionBankNotFoundException(id);
        }
        questionBank.getId();
        questionBank.getDescription();
        Hibernate.initialize(questionBank.getQuestions());
        questionBank.getQuestions().forEach((que) -> Hibernate.initialize(que.getAnnotations()));
        return questionBank;
    }

    @Override
    public Question findQuestion(final int bankId, final int questionId) {
        QuestionBank questionBank = questionBankRepository.findOne(bankId);
        if (questionBank == null)
            return null;

        if (questionBank.getQuestions().stream().map(q -> q.getId()).filter(q -> q == questionId).findFirst().isPresent()) {
            return questionRepository.findOne(questionId);
        }
        return null;
    }

    @Override
    public Page<QuestionBank> findBySearchTerm(final String searchTerm, final Pageable pageRequest) {
        Specification<QuestionBank> searchSpec = QuestionBankSpecification.descriptionContainsIgnoreCase(searchTerm);
        return questionBankRepository.findAll(searchSpec, pageRequest);
    }

    @Override
    public Page<QuestionBank> browseBanks(Pageable pageRequest) {
        return questionBankRepository.findAll(pageRequest);
    }

    @Override
    public QuestionBank createQuestionBank(QuestionBank questionBank) {
        return questionBankRepository.save(questionBank);
    }

    @Override
    public void updateQuestionBankAddQuestion(final int questionBankId, @NotNull Question question) {
        QuestionBank questionBank = questionBankRepository.getOne(questionBankId);
        if(questionBank == null)
        {
            throw new QuestionBankNotFoundException(questionBankId);
        }

        if(question.getId() == null) {
            question = questionRepository.saveAndFlush(question);
        }

        questionBank.add(question);

    }

    @Override
    public void updateQuestionBankRemoveQuestion(@NotNull final QuestionBank questionBank, final int questionId) {
        if (questionBank.getQuestions().removeIf(q -> q.getId() == questionId))
            questionBankRepository.save(questionBank);
        else {
            throw new QuestionNotFoundException(questionId);
        }
    }

    @Override
    public void deleteQuestionBank(int id) {
        if (!questionBankRepository.exists(id))
            throw new QuestionBankNotFoundException(id);
        questionBankRepository.delete(id);
    }

    @Override
    public List<QuestionBank> findByContainsQuestion(final int questionId) {
            return questionBankRepository.findByQuestionsContaining(questionId);
    }
}
