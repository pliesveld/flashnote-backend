package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Deck;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends PagingAndSortingRepository<Deck,Integer>, JpaSpecificationExecutor<Deck> {
    List<Deck> findByOwner(int id);
//    List<Deck> findByAuthor_Id(int id);
}
