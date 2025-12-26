package com.yowyob.ba.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("advertiser_profile")
public class AdvertiserProfile {
    @Id
    private UUID id;

    // Lien vers l'utilisateur qui détient le profil
    @Column("user_id")
    private UUID userId;

    @Column("company_name")
    private  String companyName;

    @Column("account_balance")
    private Double accountBalance;
}
