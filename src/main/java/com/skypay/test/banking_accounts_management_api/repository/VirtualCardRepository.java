package com.skypay.test.banking_accounts_management_api.repository;

import com.skypay.test.banking_accounts_management_api.model.VirtualCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirtualCardRepository extends JpaRepository<VirtualCard,String> {
    VirtualCard findVirtualCardByCardNumber(String cardNumber);
}