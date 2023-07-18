package com.skypay.test.banking_accounts_management_api.controller;


import com.skypay.test.banking_accounts_management_api.dto.UserDTO;
import com.skypay.test.banking_accounts_management_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/details")
    public ResponseEntity<UserDTO> getUserPersonalDetails(@RequestHeader("user-uuid") UUID userUUIDd) {
        return ResponseEntity.ok(userService.getUserInformations(userUUIDd));
    }
}