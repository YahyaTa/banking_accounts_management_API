package com.skypay.test.banking_accounts_management_api.dto;


import com.skypay.test.banking_accounts_management_api.model.Address;
import com.skypay.test.banking_accounts_management_api.model.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "First name cannot be blank")
    private String firstname;

    @NotBlank(message = "Last name cannot be blank")
    private String lastname;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Birthdate cannot be null")
    @Past(message = "Birthdate must be in the past")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid birthdate format. Please use yyyy-MM-dd")
    private Date birthdate;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^(\\+\\d{1,2})?\\d{10,}$", message = "Invalid phone number format")
    private String phoneNumber;

    @NotNull(message = "Birth address cannot be null")
    private Address birthAddress;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotNull(message = "Role cannot be null")
    private Role role = Role.User;
}