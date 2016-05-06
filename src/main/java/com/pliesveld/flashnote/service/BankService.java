package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.exception.QuestionBankNotFoundException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
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

    Question findQuestion(int qb_id, int que_id);

    Page<QuestionBank> browseBanks(Pageable pageRequest);

    QuestionBank findQuestionBankById(int id)   throws QuestionBankNotFoundException;

    Page<QuestionBank> findBySearchTerm(String searchTerm, Pageable pageRequest);

    @Transactional
    QuestionBank createQuestionBank(QuestionBank questionBank);

    @Transactional
    void deleteQuestionBank(int id)             throws QuestionBankNotFoundException;

    @Transactional
    void updateQuestionBankAddQuestion(@NotNull QuestionBank questionBank, @NotNull Question question);

    @Transactional
    void updateQuestionBankRemoveQuestion(@NotNull QuestionBank questionBank, int que_id) throws QuestionNotFoundException;

}
