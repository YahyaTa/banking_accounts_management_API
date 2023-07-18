package com.skypay.test.banking_accounts_management_api.service.UserDetailsService;


import com.skypay.test.banking_accounts_management_api.exception.AccountNotFoundException;
import com.skypay.test.banking_accounts_management_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        UserDetails user = userRepository.findUserByPhoneNumber(phoneNumber).orElseThrow(() -> new AccountNotFoundException("User not found"));
        if (user == null) {
            throw new UsernameNotFoundException("User not found with phoneNumber: " + phoneNumber);
        }

        return user;
    }
}
