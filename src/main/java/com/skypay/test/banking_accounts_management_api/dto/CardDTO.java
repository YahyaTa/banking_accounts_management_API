package com.skypay.test.banking_accounts_management_api.dto;

import com.skypay.test.banking_accounts_management_api.model.Card;
import com.skypay.test.banking_accounts_management_api.model.PhysicalCard;
import com.skypay.test.banking_accounts_management_api.model.VirtualCard;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDTO {

    private CardType cardType;
    private String cardNumber;
    private Date expirationDate;
    private Date creationDate;
    private int cvc;
    private boolean isEnabled;

    public CardDTO(PhysicalCard card) {
        this.cardType = CardType.PHYSICAL;
        this.cardNumber = card.getCardNumber();
        this.expirationDate = card.getExpirationDate();
        this.creationDate = card.getCreationDate();
        this.cvc = card.getCvc();
        this.isEnabled = card.isEnabled();
    }

    public CardDTO(VirtualCard card) {
        this.cardType = CardType.VIRTUAL;
        this.cardNumber = card.getCardNumber();
        this.expirationDate = card.getExpirationDate();
        this.creationDate = card.getCreationDate();
        this.cvc = card.getCvc();
        this.isEnabled = card.isEnabled();
    }

}

