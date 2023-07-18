package com.skypay.test.banking_accounts_management_api.dto;

import com.skypay.test.banking_accounts_management_api.model.Address;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private Date birthdate;
    private Address birthAddress;
}
