package com.skypay.test.banking_accounts_management_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skypay.test.banking_accounts_management_api.dto.UserDTO;
import com.skypay.test.banking_accounts_management_api.exception.AccountNotFoundException;
import com.skypay.test.banking_accounts_management_api.model.Account;
import com.skypay.test.banking_accounts_management_api.model.User;
import com.skypay.test.banking_accounts_management_api.repository.AccountRepository;
import com.skypay.test.banking_accounts_management_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;


    public UserDTO getUserInformations(UUID userUUIDd){
        var user = userRepository.findUserByUuid(userUUIDd).orElseThrow(()-> new AccountNotFoundException("User not found"));
        return UserDTO.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .birthdate(user.getBirthdate())
                .birthAddress(user.getBirthAddress())
                .build();
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(UUID userUUID) {
        User user = userRepository.findUserByUuid(userUUID).orElseThrow(() -> new AccountNotFoundException("User not found with UUID: " + userUUID));

        userRepository.delete(user);
    }

    @Transactional
    public void createUserWithAccount(User user, Account account) {
        // Set up the relationship between User and Account
        user.setUserAccount(account);
        account.setUser(user);

        // Save both User and Account within the same transaction
        User savedUser = userRepository.save(user);
        Account savedAccount = accountRepository.save(account);

//        //Debugging
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        // Enable pretty printing
//        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//        try {
//            // Convert the list to JSON
//            String json = objectMapper.writeValueAsString(savedUser);
//            System.out.println("savedUser = ");
//            System.out.println(json);
//
//            String json1 = objectMapper.writeValueAsString(savedAccount);
//            System.out.println("savedAccount = ");
//            System.out.println(json);
//
////                String json1 = objectMapper.writeValueAsString(account);
////                System.out.println("account = ");
////                System.out.println(json1);
//            // Print the JSON to the console
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        //Debugging


    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    public Account createAccountForUser(User user) {
        Account account = Account.builder()
                .rib(generateRibForUser(user))
                .accountNumber(generateAccountNumberForUser(user))
                .creationDate(new Date())
                .isActive(true)
                .user(user)
                .build();

        return accountRepository.save(account);
    }

    private String generateRibForUser(User user) {
        String agencyCode = String.format("%03d", (int) (Math.random() * 1000));
        String accountNumber = String.format("%016d", user.getUuid().getMostSignificantBits());
        return "181" + agencyCode + accountNumber + "27";
    }

    private Long generateAccountNumberForUser(User user) {
        return user.getUuid().getMostSignificantBits();
    }
}