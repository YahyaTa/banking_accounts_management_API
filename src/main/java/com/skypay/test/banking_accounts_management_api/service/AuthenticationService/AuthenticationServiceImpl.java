package com.skypay.test.banking_accounts_management_api.service.AuthenticationService;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypay.test.banking_accounts_management_api.config.Security.JwtService;
import com.skypay.test.banking_accounts_management_api.config.Security.Token.Token;
import com.skypay.test.banking_accounts_management_api.config.Security.Token.TokenRepository;
import com.skypay.test.banking_accounts_management_api.config.Security.Token.TokenType;
import com.skypay.test.banking_accounts_management_api.dto.AuthenticationRequest;
import com.skypay.test.banking_accounts_management_api.dto.AuthenticationResponse;
import com.skypay.test.banking_accounts_management_api.dto.RegisterRequest;
import com.skypay.test.banking_accounts_management_api.exception.AccountNotFoundException;
import com.skypay.test.banking_accounts_management_api.model.User;
import com.skypay.test.banking_accounts_management_api.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        var user = User.builder()
                            .firstname(request.getFirstname())
                            .lastname(request.getLastname())
                            .email(request.getEmail())
                            .phoneNumber(request.getPhoneNumber())
                            .password(passwordEncoder.encode(request.getPassword()))
                            .role(request.getRole())
                            .birthdate(request.getBirthdate())
                            .birthAddress(request.getBirthAddress())
                            .build();

        var savedUser = userRepository.save(user);

        var jwtToken = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(user);

        saveUserToken(savedUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getPhoneNumber(), request.getPassword()));

        var user = userRepository.findUserByPhoneNumber(request.getPhoneNumber()).orElseThrow(() -> new AccountNotFoundException("User not found"));

        var jwtToken = jwtService.generateToken(user);

        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userUUID(user.getUuid())
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {

        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllByUser_PhoneNumberAndExpiredFalseAndRevokedFalse(user.getUsername());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userPhoneNumber;

        if (authHeader == null ||!authHeader.startsWith("Bearer ")) { return; }

        refreshToken = authHeader.substring(7);

        userPhoneNumber = jwtService.extractUsername(refreshToken);

        if (userPhoneNumber != null) {

            var user = this.userRepository.findUserByPhoneNumber(userPhoneNumber).orElseThrow(() -> new AccountNotFoundException("User not found"));

            if (jwtService.isTokenValid(refreshToken, user)) {

                var accessToken = jwtService.generateToken(user);

                revokeAllUserTokens(user);

                saveUserToken(user, accessToken);

                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
