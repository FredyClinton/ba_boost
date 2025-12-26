package com.yowyob.ba.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.Version;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("campaigns")
public class Campaign {


    @Id
    private UUID id;
    // Lien vers le profil anonceur
    private UUID advertiserProfileId;

    @Column("min_age")
    private  Integer minAge;
    @Column("max_age")
    private  Integer maxAge;

    private  String city;
    private  String country;
    @Column("target_interests")

    @Builder.Default
    private Set<String> targetInterests = new HashSet<String>();
    @Column("budget_remaining")
    private  Double budgetRemaining;
    @Column("bid_amount")
    private  Double bidAmount;

    private String status;



    @Version
    @JsonIgnore
    private Long version;




}
