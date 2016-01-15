package com.pliesveld.flashnote.dao;

import com.pliesveld.flashnote.domain.Deck;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class DeckDao extends GenericDao<Deck> {
    public DeckDao() {
        setPersistentClass(Deck.class);
    }
}