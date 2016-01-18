package com.pliesveld.flashnote.dao;


import com.pliesveld.flashnote.domain.Question;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public class FlashCardDao extends GenericDao<Question> {
    public FlashCardDao() {
        setPersistentClass(Question.class);
    }

    @Transactional(readOnly = true)
    public int getCount()
    {
        Session session = sessionFactory.getCurrentSession();
        Query query = session.getNamedQuery("FlashCard.count");
        return query.uniqueResult().hashCode();
    }
}
