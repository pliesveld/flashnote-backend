package com.pliesveld.flashnote.dao;

import com.pliesveld.flashnote.domain.Deck;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class DeckDao extends GenericDao<Deck> {
    public DeckDao() {
        setPersistentClass(Deck.class);
    }


    @Transactional(readOnly = true)
    public int getCount()
    {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.getNamedQuery("Deck.count");
        return query.uniqueResult().hashCode();
    }

}