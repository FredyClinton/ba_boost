package com.yowyob.ba.repository;


import com.yowyob.ba.entity.Campaign;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface CampaignRepository extends ReactiveCrudRepository<Campaign, UUID> {

    @Query("SELECT * FROM campaigns c WHERE (c.city = :city OR c.city IS NULL)  AND (c.status = 'ACTIVE') AND (:age >= c.min_age AND :age <= c.max_age)")
    Flux<Campaign> findEligibleCampaigns(Integer age, String city);
}

// AND (c.country = :country OR c.country IS NULL)
