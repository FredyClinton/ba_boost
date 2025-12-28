package com.yowyob.ba.repository;


import com.yowyob.ba.entity.Campaign;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CampaignRepository extends ReactiveCrudRepository<Campaign, UUID> {

    // select campaigns for user
    @Query("SELECT * FROM campaigns c WHERE (c.city = :city OR c.city IS NULL)  AND (c.status = 'ACTIVE') AND (:age >= c.min_age AND :age <= c.max_age) " +
    "AND (c.start_date IS NULL OR c.start_date <= NOW()) AND (c.end_date IS NULL OR c.end_date >= NOW()))")
    Flux<Campaign> findEligibleCampaigns(Integer age, String city);

    // reduit le budget lorsqu'un fait une impression
    @Modifying
    @Query("UPDATE campaigns SET budget_remaining = budget_remaining - :cost, " +
    "status = CASE WHEN (budget_remaining - :cost) < bid_amount THEN 'PAUSED' ELSE status END " +
    "WHERE id = :id AND budget_remaining >= :cost")
    Mono<Integer> deductBudget(UUID id, Double cost);

    @Modifying
    @Query("UPDATE campaigns SET status = 'TERMINATED' WHERE status = 'ACTIVE' AND end_date < NOW()")
    Mono<Integer> terminateExpiredCampaigns();
}


