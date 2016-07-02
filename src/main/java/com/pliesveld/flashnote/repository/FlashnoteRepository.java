package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.security.StudentPrincipal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * All JPA repositories inherited from this interface support the ACL method to retrieve the
 * permissions available to them for this resource.
 * <p>
 * Implementing classes must extend SimpleJpaRepository or QueryDslJpaRepository
 * <p>
 * // TODO: Create custom JPA repository factory bean to replace internal JPA factory so that Spring Data will use the FlashnoteRepositoryImpl as the generated repositories base class
 */

@NoRepositoryBean
public interface FlashnoteRepository<T, ID extends Serializable>
        extends CrudRepository<T, ID> {
    enum RepositoryPermissions { NONE, READ, WRITE }

    public RepositoryPermissions permissions(StudentPrincipal currentUser, T resource);
}
