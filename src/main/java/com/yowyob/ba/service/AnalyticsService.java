package com.yowyob.ba.service;

import com.yowyob.ba.dto.CampaignStats;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface AnalyticsService {
    public Mono<CampaignStats> getCampaignStats(UUID campaignId);
}
