package com.yowyob.ba.repository;

import com.yowyob.ba.entity.Interaction;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AnalyticsRepository extends ReactiveCrudRepository<Interaction, UUID> {

    @Query("SELECT COUNT(*) FROM interactions WHERE campaign_id = :campaignId AND interaction_type = :type")
    Mono<Long> countInteractions(UUID campaignId, String type);
}
