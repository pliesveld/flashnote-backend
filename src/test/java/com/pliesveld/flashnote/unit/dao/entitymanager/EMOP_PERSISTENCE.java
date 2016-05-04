package com.pliesveld.flashnote.unit.dao.entitymanager;

import com.pliesveld.flashnote.domain.base.DomainBaseEntity;

import javax.persistence.EntityManager;

/**
* Created by happs on 4/30/16.
*/
public enum EMOP_PERSISTENCE implements EMOP
{
    FLUSH {
        public void apply(EntityManager entityManager) {
            entityManager.flush();
        }
    },
    CLEAR {
        @Override
        public void apply(EntityManager entityManager) {
            entityManager.clear();
        }
    };

    @Override
    public abstract void apply(EntityManager entityManager);

    @Override
    public void apply(EntityManager entityManager, DomainBaseEntity entity) {
        this.apply(entityManager);
    }

}
