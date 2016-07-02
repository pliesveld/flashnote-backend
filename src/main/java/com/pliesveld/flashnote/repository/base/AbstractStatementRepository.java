package com.pliesveld.flashnote.repository.base;


import com.pliesveld.flashnote.domain.AbstractStatement;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.time.Instant;
import java.util.stream.Stream;

@NoRepositoryBean
public interface AbstractStatementRepository<T extends AbstractStatement>
        extends Repository<T, Integer>, JpaSpecificationExecutor<T> {
    @Query("select t from #{#entityName} t where t.id = ?1")
    T findOneById(int id);

    @Query("select t from #{#entityName} t where t.createdByUser = ?1")
    Stream<T> findAllByAuthor(String author);

    @Query("select t from #{#entityName} t where t.createdOn > ?1")
    Stream<T> findAllByCreatedSince(Instant past);

    @Query("select t from #{#entityName} t where t.createdOn < ?1")
    Stream<T> findAllByCreatedBefore(Instant future);


}
