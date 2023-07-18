package com.skypay.test.banking_accounts_management_api.controller;

import com.skypay.test.banking_accounts_management_api.dto.AccountDTO;
import com.skypay.test.banking_accounts_management_api.dto.UserDTO;
import com.skypay.test.banking_accounts_management_api.service.AccountService;
import com.skypay.test.banking_accounts_management_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/user/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/details")
    public ResponseEntity<AccountDTO> getUserAccountDetails(@RequestHeader("user-uuid") UUID userUUID) {
        return ResponseEntity.ok(accountService.getUserInformations(userUUID));
    }
}