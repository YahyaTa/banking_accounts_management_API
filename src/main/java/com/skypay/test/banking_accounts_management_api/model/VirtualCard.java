package com.skypay.test.banking_accounts_management_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VirtualCard implements Card {
    @Id
    private String cardNumber;
    private Date expirationDate;
    private Date creationDate;
    private int cvc;
    private boolean isEnabled;

}
