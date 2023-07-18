package com.skypay.test.banking_accounts_management_api.service;

import com.skypay.test.banking_accounts_management_api.dto.CardDTO;
import com.skypay.test.banking_accounts_management_api.dto.CardStatus;
import com.skypay.test.banking_accounts_management_api.dto.CardStatusDTO;
import com.skypay.test.banking_accounts_management_api.exception.AccountNotFoundException;
import com.skypay.test.banking_accounts_management_api.exception.UserNotFoundException;
import com.skypay.test.banking_accounts_management_api.model.PhysicalCard;
import com.skypay.test.banking_accounts_management_api.model.User;
import com.skypay.test.banking_accounts_management_api.model.VirtualCard;
import com.skypay.test.banking_accounts_management_api.repository.PhysicalCardRepository;
import com.skypay.test.banking_accounts_management_api.repository.UserRepository;
import com.skypay.test.banking_accounts_management_api.repository.VirtualCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private final UserRepository userRepository;
    private final VirtualCardRepository virtualCardRepository;
    private final PhysicalCardRepository physicalCardRepository;



    public CardStatusDTO changeCardStatus(UUID uuid, String cardNumber, CardStatus status) {

        User user = userRepository.findUserByUuid(uuid).orElseThrow(()-> new UserNotFoundException("User not found"));

        // Check if the user account have a physical card with the given card number
        if(user.getUserAccount().getPhysicalCard().getCardNumber().equals(cardNumber)) {

            PhysicalCard card = physicalCardRepository.findPhysicalCardByCardNumber(cardNumber);

            card.setEnabled(true);

            PhysicalCard savedCard = physicalCardRepository.save(card);

            if(!savedCard.equals(null)) return  new CardStatusDTO(status);

        // Check if the user account have a virtual card with the given card number
        } else if(user.getUserAccount().getVirtualCards().stream().anyMatch(virtualCard -> virtualCard.getCardNumber().equals(cardNumber))) {

            VirtualCard card = virtualCardRepository.findVirtualCardByCardNumber(cardNumber);

            card.setEnabled(true);

            VirtualCard savedCard = virtualCardRepository.save(card);

            if(!savedCard.equals(null)) return  new CardStatusDTO(status);

        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND,"It seems that you don't have a card with the number :" + cardNumber +"." );
    }

    public CardDTO createVirtualCard(UUID uuid) {
        User user = userRepository.findUserByUuid(uuid).orElseThrow(() -> new AccountNotFoundException("User not found"));

        // Generate a unique virtual card number
        String virtualCardNumber = generateUniqueVirtualCardNumber(user);

        // Generate expiration date (2 years from the creation date)
        Date expirationDate = calculateExpirationDate();

        // Generate CVC (three random digits)
        int cvc = generateRandomCVC();

        // Create the virtual card
        VirtualCard virtualCard = VirtualCard.builder()
                .cardNumber(virtualCardNumber)
                .expirationDate(expirationDate)
                .creationDate(new Date())
                .cvc(cvc)
                .isEnabled(false) // Assuming the card is disabled by default
                .build();

        // Save the virtual card to the user's account
        user.getUserAccount().getVirtualCards().add(virtualCard);
        userRepository.save(user);

        // Return the new virtual card information
        return new CardDTO(virtualCard);
    }

    public String generateUniqueVirtualCardNumber(User user) {
        String userUUIDString = user.getUuid().toString().replace("-", "");
        String randomNumber = getRandomNumberAsString(6);
        String combinedString = userUUIDString + randomNumber;

        // Using SHA-256 hash function to create a unique card number
        String hashedCardNumber = getSHA256Hash(combinedString);

        // Taking the first 16 characters of the hash if you want a shorter card number
        return hashedCardNumber.substring(0, 16);
    }


    public Date calculateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2); // Add 2 years to the current date
        return calendar.getTime();
    }

    public int generateRandomCVC() {
        Random random = new Random();
        return 100 + random.nextInt(900); // Generate a random 3-digit number
    }

    private String getRandomNumberAsString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String getSHA256Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder sb = new StringBuilder(number.toString(16));
            while (sb.length() < 32) {
                sb.insert(0, '0');
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to generate SHA-256 hash", e);
        }
    }

    public String deleteVirtualCard(UUID uuid, String cardNumber) {

        User user = userRepository.findUserByUuid(uuid).orElseThrow(()-> new UserNotFoundException("User not found"));

        if(user.getUserAccount().getVirtualCards().stream().anyMatch(virtualCard -> virtualCard.getCardNumber().equals(cardNumber))) {

            virtualCardRepository.deleteById(cardNumber);

            return "Card deleted successfully";

        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND,"It seems that you don't have a card with the number :" + cardNumber +".");


    }
}
