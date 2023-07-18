package com.skypay.test.banking_accounts_management_api.config.Security.Token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    List<Token> findAllByUser_PhoneNumberAndExpiredFalseAndRevokedFalse(String phoneNumber);

    Optional<Token> findByToken(String token);
}

