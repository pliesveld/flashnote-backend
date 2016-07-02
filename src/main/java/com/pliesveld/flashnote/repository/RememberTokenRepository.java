package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AccountRememberMeToken;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RememberTokenRepository extends CrudRepository<AccountRememberMeToken,String> {

    @Modifying
    @Query("delete AccountRememberMeToken t where t.username = ?1")
    void deleteTokensForUser(String username);
}
