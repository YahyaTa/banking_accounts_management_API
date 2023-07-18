package com.skypay.test.banking_accounts_management_api.config.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.javafaker.Faker;
import com.skypay.test.banking_accounts_management_api.exception.FailedToCreateFakeDataException;
import com.skypay.test.banking_accounts_management_api.model.*;
import com.skypay.test.banking_accounts_management_api.repository.AccountRepository;
import com.skypay.test.banking_accounts_management_api.repository.PhysicalCardRepository;
import com.skypay.test.banking_accounts_management_api.repository.UserRepository;
import com.skypay.test.banking_accounts_management_api.repository.VirtualCardRepository;
import com.skypay.test.banking_accounts_management_api.service.CardService;
import com.skypay.test.banking_accounts_management_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.util.*;

@Component
public class FakeDataSeeder {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final VirtualCardRepository virtualCardRepository;
    private final PhysicalCardRepository physicalCardRepository;
    private final CardService cardService;
    private final UserService userService;

    private final Faker faker = new Faker();

    public FakeDataSeeder (
            UserRepository userRepository,
            AccountRepository accountRepository,
            VirtualCardRepository virtualCardRepository,
            PhysicalCardRepository physicalCardRepository,
            CardService cardService,
            UserService userService
) {

        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.virtualCardRepository = virtualCardRepository;
        this.physicalCardRepository = physicalCardRepository;
        this.cardService = cardService;
        this.userService = userService;
    }


    @PostConstruct
    public void seedData() {
        seedUsers();
    }

    private void seedUsers() {
        List<User> users = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            String agencyNumber = generateAgencyNumber();
            String accountNumber = generateAccountNumber();
            String rib = "181" + agencyNumber + accountNumber + "27";

            User user = User.builder()
                    .uuid(UUID.randomUUID())
                    .firstname(faker.name().firstName())
                    .lastname(faker.name().lastName())
                    .email(faker.internet().emailAddress())
                    .phoneNumber(faker.phoneNumber().phoneNumber())
                    .birthdate(faker.date().birthday())
                    .birthAddress(generateAddress())
                    .password("password")
                    .role(Role.User)
                    .build();


            Account account = Account.builder()
                    .rib(rib)
                    .accountNumber(Long.parseLong(accountNumber))
                    .creationDate(new Date())
                    .isActive(true)
                    .agencyAddress(generateAddress())
                    .build();


            users.add(user);

            //Debugging
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            // Enable pretty printing
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//            try {
//                // Convert the list to JSON
//                String json = objectMapper.writeValueAsString(user);
//                System.out.println("user = ");
//                System.out.println(json);
//
//                String json1 = objectMapper.writeValueAsString(account);
//                System.out.println("account = ");
//                System.out.println(json1);
//                // Print the JSON to the console
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
            //Debugging

            // Seed virtual cards for the account
            seedVirtualCards(user,account);

            // Seed a physical card for the account
            seedPhysicalCard(user,account);
        }


//        var savedUsers = userRepository.saveAll(users);
//        //Debugging
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            // Enable pretty printing
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//            try {
//                // Convert the list to JSON
//                String json = objectMapper.writeValueAsString(savedUsers);
//                System.out.println("savedUsers = ");
//                System.out.println(json);
//
////                String json1 = objectMapper.writeValueAsString(account);
////                System.out.println("account = ");
////                System.out.println(json1);
//                // Print the JSON to the console
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//        //Debugging
        // Seed accounts and cards for each user

//        if (savedUsers.isEmpty()) {
//            throw new FailedToCreateFakeDataException("Failed to save fake users to database");
//        }



    }



    private String generateAgencyNumber() {
        // Generate a random three-digit agency number
        return String.format("%03d", faker.number().numberBetween(1, 1000));
    }

    private String generateAccountNumber() {
        // Generate a random 16-digit account number
        return String.format("%016d", faker.number().numberBetween(1L, Long.MAX_VALUE));
    }

    private void seedVirtualCards(User user, Account account) {
        int numberOfCards = faker.number().numberBetween(1, 4); // Generate 1 to 3 virtual cards
        Set<VirtualCard> virtualCards = new LinkedHashSet<>();
        for (int i = 0; i < numberOfCards; i++) {
            String cardNumber = cardService.generateUniqueVirtualCardNumber(user);
            int cvc = cardService.generateRandomCVC();

            VirtualCard virtualCard = VirtualCard.builder()
                    .cardNumber(cardNumber)
                    .expirationDate(cardService.calculateExpirationDate())
                    .creationDate(new Date())
                    .cvc(cvc)
                    .isEnabled(true)
                    .build();

//            //Debugging
//            ObjectMapper objectMapper = new ObjectMapper();
//
//            // Enable pretty printing
//            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
//
//            try {
//                // Convert the list to JSON
//
//                String json2 = objectMapper.writeValueAsString(account);
//                System.out.println("account = ");
//                System.out.println(json2);
//
//                String json1 = objectMapper.writeValueAsString(user);
//                System.out.println("user = ");
//                System.out.println(json1);
//
//                String json = objectMapper.writeValueAsString(virtualCard);
//                System.out.println("virtualCard = ");
//                System.out.println(json);
//
//
//                // Print the JSON to the console
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//            //Debugging

            virtualCards.add(virtualCard);
//            account.addVirtualCard(virtualCard);

        }

        account.setVirtualCards(virtualCards);

        userService.createUserWithAccount(user,account);

    }

    private void seedPhysicalCard(User user, Account account) {
        String cardNumber = cardService.generateUniqueVirtualCardNumber(user);
        int cvc = cardService.generateRandomCVC();

        PhysicalCard physicalCard = (PhysicalCard) PhysicalCard.builder()
                .cardNumber(cardNumber)
                .expirationDate(cardService.calculateExpirationDate())
                .creationDate(new Date())
                .cvc(cvc)
                .isEnabled(true)
                .build();

        account.setPhysicalCard(physicalCard);

        userService.createUserWithAccount(user,account);

    }


    private Address generateAddress() {
        return Address.builder()
                .street(faker.address().streetAddress())
                .city(faker.address().city())
                .postalCode(faker.address().zipCode())
                .country(faker.address().country())
                .build();
    }


}
