package com.yowyob.ba.service;

import com.yowyob.ba.entity.Campaign;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface CampaignService {

    Mono<Campaign> createCampaign(Campaign campaign);
    Mono<Campaign> getCampaignById(UUID id);
    Flux<Campaign> getAllCampaigns();
    Mono<Campaign> updateCampaign(UUID id, Campaign campaignDetails);
    Mono<Void> deleteCampaign(UUID id);
}
