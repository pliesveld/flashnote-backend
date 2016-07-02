package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AccountRegistrationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.stream.Stream;


public interface RegistrationRepository extends JpaRepository<AccountRegistrationToken, Integer> {
    AccountRegistrationToken findByToken(String token);

    @Query("select a from AccountRegistrationToken a")
    Stream<AccountRegistrationToken> findAllAsStream();

    Stream<AccountRegistrationToken> findAllByExpirationLessThan(Instant now);
}
