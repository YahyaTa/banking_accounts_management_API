package com.skypay.test.banking_accounts_management_api.service;


import com.skypay.test.banking_accounts_management_api.dto.AccountDTO;
import com.skypay.test.banking_accounts_management_api.exception.AccountNotFoundException;
import com.skypay.test.banking_accounts_management_api.model.Account;
import com.skypay.test.banking_accounts_management_api.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountDTO getUserInformations(UUID userUUIDd) {


        Account account = accountRepository.findByUserUuid(userUUIDd).orElseThrow(()-> new AccountNotFoundException("Account not found"));

        return new AccountDTO(account);
    }
}