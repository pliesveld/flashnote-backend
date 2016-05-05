package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.exception.QuestionBankNotFoundException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import com.pliesveld.flashnote.repository.QuestionBankRepository;
import com.pliesveld.flashnote.repository.specifications.QuestionBankSpecification;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service("bankService")
public class BankServiceImpl implements BankService {

    @Autowired
    QuestionBankRepository questionBankRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<QuestionBank> findAllQuestionBanks() {
        ArrayList<QuestionBank> list = new ArrayList<>();
        questionBankRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    public QuestionBank createQuestionBank(QuestionBank questionBank) {
        return questionBankRepository.save(questionBank);
    }

    @Override
    public QuestionBank findQuestionBankById(int id) {
        QuestionBank questionBank = questionBankRepository.findOne(id);
        if(questionBank == null)
        {
            throw new QuestionBankNotFoundException(id);
        }
        questionBank.getId();
        questionBank.getDescription();
        Hibernate.initialize(questionBank.getQuestions());
        questionBank.getQuestions().forEach((que) -> Hibernate.initialize(que.getAnnotations()));
        return questionBank;
    }

    @Override
    public void deleteQuestionBank(int id) {
        if(!questionBankRepository.exists(id))
            throw new QuestionNotFoundException(id);
        questionBankRepository.delete(id);
    }

    @Override
    public Page<QuestionBank> findBySearchTerm(String searchTerm, Pageable pageRequest) {
        Specification<QuestionBank> searchSpec = QuestionBankSpecification.descriptionContainsIgnoreCase(searchTerm);
        return questionBankRepository.findAll(searchSpec, pageRequest);
    }
}
