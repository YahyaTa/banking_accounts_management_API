package com.skypay.test.banking_accounts_management_api.controller;

import com.skypay.test.banking_accounts_management_api.dto.CardDTO;
import com.skypay.test.banking_accounts_management_api.dto.CardStatus;
import com.skypay.test.banking_accounts_management_api.dto.CardStatusDTO;
import com.skypay.test.banking_accounts_management_api.service.AccountService;
import com.skypay.test.banking_accounts_management_api.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/user/account/cards/")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/enable")
    public ResponseEntity<CardStatusDTO> enableCard(@RequestHeader("user-uuid") UUID uuid, @RequestParam String cardNumber) {
        CardStatusDTO cardStatusDTO = cardService.changeCardStatus(uuid, cardNumber, CardStatus.ENABLED);
        return ResponseEntity.ok(cardStatusDTO);
    }

    @PostMapping("/disable")
    public ResponseEntity<CardStatusDTO> disableCard(@RequestHeader("user-uuid") UUID uuid, @RequestParam String cardNumber) {
        CardStatusDTO cardStatusDTO = cardService.changeCardStatus(uuid, cardNumber, CardStatus.DISABLED);
        return ResponseEntity.ok(cardStatusDTO);
    }

    @PostMapping("/virtual-cards/create")
    public ResponseEntity<CardDTO> createVirtualCard(@RequestHeader("user-uuid") UUID uuid) {
        CardDTO virtualCardDTO = cardService.createVirtualCard(uuid);
        return ResponseEntity.ok(virtualCardDTO);
    }

    @DeleteMapping("/virtual-cards/delete")
    public ResponseEntity<String> deleteVirtualCard(@RequestHeader("user-uuid") UUID uuid, @RequestParam String cardNumber) {
        String message = cardService.deleteVirtualCard(uuid,cardNumber);
        return ResponseEntity.ok(message);
    }

}