package com.pliesveld.flashnote.repository;

import com.pliesveld.flashnote.domain.AccountPasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.stream.Stream;


public interface PasswordResetRepository extends JpaRepository<AccountPasswordResetToken, Integer> {
    AccountPasswordResetToken findByToken(String token);
    AccountPasswordResetToken findByStudent_id(Integer id);
    Stream<AccountPasswordResetToken> findAllByExpirationLessThan(Instant now);
}
