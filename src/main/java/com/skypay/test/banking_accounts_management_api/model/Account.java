package com.skypay.test.banking_accounts_management_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

    private String rib;

    @Id
    private Long accountNumber;
    private Date creationDate;
    private boolean isActive;

    @Embedded
    private Address agencyAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private PhysicalCard physicalCard;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<VirtualCard> virtualCards = new LinkedHashSet<>() ;
//            new TreeSet<>((c1, c2) -> c1.getCreationDate().compareTo(c2.getCreationDate()));

    @OneToOne(mappedBy = "userAccount")
//            (mappedBy = "userAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private User user;

    public void addVirtualCard(VirtualCard virtualCard) {
        this.virtualCards.add(virtualCard);
    }

}
