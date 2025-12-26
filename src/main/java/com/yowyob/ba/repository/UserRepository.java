package com.yowyob.ba.repository;

import com.yowyob.ba.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface UserRepository extends ReactiveCrudRepository<User, UUID> {


    // recherche les utilisateurs par criteres demographiques de base.
    @Query("SELECT * FROM users u" +
            "WHERE u.age >= :minAge AND u.age<= : maxAge" +
            "AND u.country = :country OR :country IS NULL"+
            "AND (u.city = :city OR :city IS NULL)")
    Flux<User> findMatchingUsers(int minAge, int maxAge, String city, String country);
}
