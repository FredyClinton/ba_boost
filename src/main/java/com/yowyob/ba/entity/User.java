package com.yowyob.ba.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("users")
public class User {

    @Id
    private UUID userId;
    private String email;
    private  Integer age;
    private String city;
    private String country;

    @Column("interests")
    private Set<String> interests;

    @Column("last_active")
    private Instant lastActive;




}
