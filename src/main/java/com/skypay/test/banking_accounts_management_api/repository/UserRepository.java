package com.skypay.test.banking_accounts_management_api.repository;

import com.skypay.test.banking_accounts_management_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,UUID> {

    Optional<User> findUserByPhoneNumber(String phoneNumber);

    Optional<User> findUserByUuid(UUID uuid);
}