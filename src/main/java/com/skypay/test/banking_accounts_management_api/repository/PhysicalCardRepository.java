package com.skypay.test.banking_accounts_management_api.repository;

import com.skypay.test.banking_accounts_management_api.model.PhysicalCard;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PhysicalCardRepository extends JpaRepository<PhysicalCard,String> {
    PhysicalCard findPhysicalCardByCardNumber(String cardNumber);

}