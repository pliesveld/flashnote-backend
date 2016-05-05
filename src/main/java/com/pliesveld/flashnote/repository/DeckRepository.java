package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Deck;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeckRepository extends CrudRepository<Deck,Integer> {
    List<Deck> findByOwner(int id);
//    List<Deck> findByAuthor_Id(int id);
}
