package com.pliesveld.tests.entitymanager;

import com.pliesveld.flashnote.domain.base.DomainBaseEntity;

import javax.persistence.EntityManager;

/**
* Created by happs on 4/30/16.
*/
public interface EMOP
{
    void apply(EntityManager entityManager);

    default void apply(EntityManager entityManager, DomainBaseEntity entity) {
        this.apply(entityManager);
    }

}
