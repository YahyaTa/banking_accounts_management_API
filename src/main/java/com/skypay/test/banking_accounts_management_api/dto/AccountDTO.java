package com.skypay.test.banking_accounts_management_api.dto;

import com.skypay.test.banking_accounts_management_api.model.Account;
import com.skypay.test.banking_accounts_management_api.model.Address;
import com.skypay.test.banking_accounts_management_api.model.Card;
import com.skypay.test.banking_accounts_management_api.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private String rib;
    private Address agencyAddress;
    private Long accountNumber;
    private boolean active;
    private List<CardDTO> cardsList = new ArrayList<>();

    public AccountDTO(Account account) {

        this.rib = account.getRib();
        this.agencyAddress = account.getAgencyAddress();
        this.accountNumber = account.getAccountNumber();
        this.active = account.isActive();
        cardsList.add(new CardDTO(account.getPhysicalCard()));
        cardsList.addAll(account.getVirtualCards().stream().map(CardDTO::new).collect(Collectors.toList()));

    }
}
