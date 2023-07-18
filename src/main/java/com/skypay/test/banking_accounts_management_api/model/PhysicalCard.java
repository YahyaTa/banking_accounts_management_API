package com.skypay.test.banking_accounts_management_api.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PhysicalCard implements Card{

    @Id
    private String cardNumber;
    private Date expirationDate;
    private Date creationDate;
    private int cvc;
    private boolean isEnabled;
}
