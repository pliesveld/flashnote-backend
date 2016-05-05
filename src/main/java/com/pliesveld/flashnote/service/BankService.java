package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.exception.QuestionBankNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Transactional(readOnly = true)
public interface BankService {

    @NotNull
    List<QuestionBank> findAllQuestionBanks();

    @Transactional
    QuestionBank createQuestionBank(QuestionBank questionBank);

    QuestionBank findQuestionBankById(int id)   throws QuestionBankNotFoundException;

    @Transactional
    void deleteQuestionBank(int id)             throws QuestionBankNotFoundException;

    Page<QuestionBank> findBySearchTerm(String searchTerm, Pageable pageRequest);
}
