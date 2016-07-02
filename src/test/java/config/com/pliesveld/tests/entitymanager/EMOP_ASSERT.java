package com.pliesveld.tests.entitymanager;

import com.pliesveld.flashnote.domain.base.DomainBaseEntity;
import org.apache.http.util.Asserts;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;

/**
 * @author Patrick Liesveld
 */
public enum EMOP_ASSERT implements EMOP {
    CONTAINS_ENTITY_TRUE {
        @Override
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            Asserts.check(entityManager.contains(entity), "entity should be in persistence context");
        }
    },
    CONTAINS_ENTITY_FALSE {
        @Override
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            Asserts.check(!entityManager.contains(entity), "entity should not be in persistence context");
        }
    },
    PROXY_INITIALIZED_TRUE {
        @Override
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            Asserts.check(Hibernate.isInitialized(entity), "entity should be initialized");
        }
    },
    PROXY_INITIALIZED_FALSE {
        @Override
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            Asserts.check(!Hibernate.isInitialized(entity), "entity should not be initialized");
        }
    },
    LOADED_TRUE {
        @Override
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            Asserts.check(entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(entity), "entity should be loaded");
        }
    },
    LOADED_FALSE {
        @Override
        public void apply(EntityManager entityManager, DomainBaseEntity entity) {
            Asserts.check(!entityManager.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(entity), "entity should not be loaded");
        }
    };

    public void apply(EntityManager entityManager) {
        throw new IllegalStateException("Need domain entity to apply " + this.toString());
    }

    public abstract void apply(EntityManager entityManager, DomainBaseEntity entity);

}
