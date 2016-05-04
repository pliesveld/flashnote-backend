package com.pliesveld.flashnote.unit.dao.entitymanager;

import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;

/**
* Created by happs on 4/30/16.
*/
public enum EMOP_ENTITY implements EMOP {
    FLUSH {
        @Override
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            entityManager.flush();
        }
    },
    PERSIST {
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            entityManager.persist(entity);
        }
    },

    MERGE {
        @Override
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            entityManager.merge(entity);
        }
    },

    DETACH {
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            entityManager.detach(entity);
        }
    },

    REMOVE {
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            if(! entityManager.isJoinedToTransaction() )
                throw new IllegalStateException("Must be in transaction to call entityManager." + this.toString());
            entityManager.remove(entity);
        }
    },

    REFRESH {
        @Override
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            if(! entityManager.isJoinedToTransaction() )
                throw new IllegalStateException("Must be in transaction to call entityManager." + this.toString());
            entityManager.refresh(entity);
        }
    },

    INITIALIZE {
        @Override
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            Hibernate.initialize(entity);
        }
    };

    @Override
    public void apply(EntityManager entityManager)
    {
        throw new IllegalStateException("Need domain entity to apply " + this.toString());
    }
    public abstract void apply(EntityManager entityManager, DomainBaseEntity entity);
}
