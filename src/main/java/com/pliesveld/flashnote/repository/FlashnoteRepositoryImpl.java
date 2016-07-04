package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.security.StudentPrincipal;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;


public class FlashnoteRepositoryImpl<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID>
        implements FlashnoteRepository<T, ID> {
    private Class<T> domainClass;
    private EntityManager entityManager;

    public FlashnoteRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.domainClass = domainClass;
        this.entityManager = entityManager;
    }

    public FlashnoteRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.domainClass = entityInformation.getJavaType();
        this.entityManager = entityManager;
    }

    @Override
    public RepositoryPermissions permissions(StudentPrincipal currentUser, T resource) {
        return RepositoryPermissions.NONE;
    }
}
