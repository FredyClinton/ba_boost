package com.yowyob.ba.controller;

import com.yowyob.ba.dto.CampaignStats;
import com.yowyob.ba.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @GetMapping("/{campaignId}")
    public Mono<CampaignStats> getStats(@PathVariable UUID campaignId) {
        return analyticsService.getCampaignStats(campaignId);
    }
}
