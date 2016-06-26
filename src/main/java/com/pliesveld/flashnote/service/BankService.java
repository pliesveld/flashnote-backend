package com.pliesveld.flashnote.service;

import com.pliesveld.flashnote.domain.Question;
import com.pliesveld.flashnote.domain.QuestionBank;
import com.pliesveld.flashnote.exception.QuestionBankNotFoundException;
import com.pliesveld.flashnote.exception.QuestionNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@Transactional(readOnly = true)
public interface BankService {

    @NotNull
    List<QuestionBank> findAllQuestionBanks();

    Question findQuestion(final int bankId, final int questionId);

    Page<QuestionBank> browseBanks(final Pageable pageRequest);

    Page<QuestionBank> browseBanksWithSpec(final Specification<QuestionBank> spec, final Pageable pageRequest);

    QuestionBank findQuestionBankById(final int bankId)   throws QuestionBankNotFoundException;

    Page<QuestionBank> findBySearchTerm(final String searchTerm, final Pageable pageRequest);

    List<QuestionBank> findByContainsQuestion(final int questionId);

    @Transactional
    QuestionBank createQuestionBank(final QuestionBank questionBank);

    @Transactional
    void deleteBank(final int bankId)             throws QuestionBankNotFoundException;

    @Transactional
    void updateQuestionBankAddQuestion(final int bankId, @NotNull Question question);

    @Transactional
    void updateQuestionBankRemoveQuestion(@NotNull final QuestionBank questionBank, final int questionId) throws QuestionNotFoundException;

    List<QuestionBank> findBanksContainingQuestion(Question question);

}
