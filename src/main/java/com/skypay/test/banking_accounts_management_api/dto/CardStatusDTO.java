package com.skypay.test.banking_accounts_management_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
public class CardStatusDTO {

    @JsonProperty("Current card status")
    private CardStatus CurrentCardStatus;
}
