package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Deck;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DeckRepository extends CrudRepository<Deck,Integer> {
    List<Deck> findByAuthor_Id(int id);
}
