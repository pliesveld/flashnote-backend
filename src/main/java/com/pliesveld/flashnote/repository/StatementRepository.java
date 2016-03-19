package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AbstractStatement;
import com.pliesveld.flashnote.repository.base.AbstractStatementRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementRepository extends AbstractStatementRepository<AbstractStatement> {
}
