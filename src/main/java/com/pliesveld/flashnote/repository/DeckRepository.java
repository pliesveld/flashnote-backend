package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeckRepository extends JpaRepository<Deck,Integer> {
}
