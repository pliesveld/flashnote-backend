package com.pliesveld.tests.entitymanager;

import com.pliesveld.flashnote.domain.base.DomainBaseEntity;

import javax.persistence.EntityManager;

/**
 * @author Patrick Liesveld
 */
public enum EMOP_PERSISTENCE implements EMOP {
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
