//package com.skypay.test.banking_accounts_management_api.repository;
//
//import com.skypay.test.banking_accounts_management_api.model.Card;
//import com.skypay.test.banking_accounts_management_api.model.PhysicalCard;
//import com.skypay.test.banking_accounts_management_api.model.VirtualCard;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.util.List;
//
//public interface CardRepository extends JpaRepository<Card, Long> {
//
//    @Query("SELECT c FROM Card c WHERE TYPE(c) = PhysicalCard")
//    List<PhysicalCard> findAllPhysicalCards();
//
//    @Query("SELECT c FROM Card c WHERE TYPE(c) = VirtualCard")
//    List<VirtualCard> findAllVirtualCards();
//
//    // Add more custom queries if needed
//}
//
