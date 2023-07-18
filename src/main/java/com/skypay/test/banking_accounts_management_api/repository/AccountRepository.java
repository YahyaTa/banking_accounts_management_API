package com.skypay.test.banking_accounts_management_api.repository;

import com.skypay.test.banking_accounts_management_api.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface AccountRepository extends JpaRepository<Account,Long> {

    Optional<Account> findByUserUuid(UUID userUUID);
}
