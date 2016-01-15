package com.pliesveld.flashnote.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
@Scope("prototype")
public abstract class GenericDao<T> {
    @Autowired
    SessionFactory sessionFactory;

    protected Class<T> persistentClass;

    public void setPersistentClass(Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public void save(T object)
    {
        Session session = sessionFactory.getCurrentSession();
        session.saveOrUpdate(object);
    }

    @SuppressWarnings("unchecked")
    public T getDomainObjectById(Integer id)
    {
        Session session = sessionFactory.getCurrentSession();
        return (T) session.get(persistentClass,id);
    }
}
