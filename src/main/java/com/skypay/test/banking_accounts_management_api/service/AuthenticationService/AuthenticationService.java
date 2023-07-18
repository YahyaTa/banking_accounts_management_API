package com.skypay.test.banking_accounts_management_api.service.AuthenticationService;

import com.skypay.test.banking_accounts_management_api.dto.AuthenticationRequest;
import com.skypay.test.banking_accounts_management_api.dto.AuthenticationResponse;
import com.skypay.test.banking_accounts_management_api.dto.RegisterRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    void refreshToken( HttpServletRequest request, HttpServletResponse response ) throws IOException;
}
