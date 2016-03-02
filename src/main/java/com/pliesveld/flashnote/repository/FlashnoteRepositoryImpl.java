package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.security.StudentPrincipal;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created by happs on 3/2/16.
 */
public class FlashnoteRepositoryImpl<T, ID extends Serializable>
    extends SimpleJpaRepository<T,ID>
    implements FlashnoteRepository<T,ID>
{
    private Class<T> domainClass;
    private EntityManager entityManager;

    public FlashnoteRepositoryImpl(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        this.domainClass = domainClass;
        this.entityManager = entityManager;
    }

    FlashnoteRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.domainClass = entityInformation.getJavaType();
        this.entityManager = entityManager;
    }


    @Override
    public PERMISSIONS permissions(StudentPrincipal currentUser, T resource) {
        return PERMISSIONS.NONE;
    }
}
