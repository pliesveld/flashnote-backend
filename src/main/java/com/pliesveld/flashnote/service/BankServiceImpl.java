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

@Service("bankService")
public class BankServiceImpl implements BankService {

    @Autowired
    QuestionBankRepository questionBankRepository;

    @Autowired
    QuestionRepository questionRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<QuestionBank> findAllQuestionBanks() {
        ArrayList<QuestionBank> list = new ArrayList<>();
        questionBankRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public QuestionBank findQuestionBankById(int id) {
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
    public Question findQuestion(int qb_id, final int que_id) {
        QuestionBank questionBank = questionBankRepository.findOne(qb_id);
        if (questionBank == null)
            return null;

        if (questionBank.getQuestions().stream().map(q -> q.getId()).filter(q -> q == que_id).findFirst().isPresent()) {
            return questionRepository.findOne(que_id);
        }
        return null;
    }

    @Override
    public Page<QuestionBank> findBySearchTerm(String searchTerm, Pageable pageRequest) {
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
    public void updateQuestionBankAddQuestion(@NotNull QuestionBank questionBank, @NotNull Question question) {

        questionBank = questionBankRepository.findOne(questionBank.getId());
        if (questionBank == null)
            throw new QuestionBankNotFoundException(questionBank.getId());

        question = questionRepository.saveAndFlush(question);
        questionBank.add(question);
    }

    @Override
    public void updateQuestionBankRemoveQuestion(@NotNull QuestionBank questionBank, int que_id) {
        if (questionBank.getQuestions().removeIf(q -> q.getId() == que_id))
            questionBankRepository.save(questionBank);
        else {
            throw new QuestionNotFoundException(que_id);
        }
    }

    @Override
    public void deleteQuestionBank(int id) {
        if (!questionBankRepository.exists(id))
            throw new QuestionNotFoundException(id);
        questionBankRepository.delete(id);
    }

}
