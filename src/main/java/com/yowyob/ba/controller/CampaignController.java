package com.yowyob.ba.controller;

import com.yowyob.ba.entity.Campaign;
import com.yowyob.ba.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;

    @GetMapping
    Flux<Campaign> getCampaigns() {
        return campaignService.getAllCampaigns();
    }

    @GetMapping("/{id}")
    Mono<Campaign> getCampaignById(@PathVariable UUID id) {
        return campaignService.getCampaignById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    Mono<Campaign> createCampaign(@RequestBody Campaign campaign) {
        return campaignService.createCampaign(campaign);
    }

    @PutMapping("/{id}")
    Mono<Campaign> updateCampaign(@PathVariable UUID id, @RequestBody Campaign campaign) {
        return campaignService.updateCampaign(id, campaign);
    }

    @DeleteMapping("/{id}")
    Mono<Void> deleteCampaign(@PathVariable UUID id) {
        return campaignService.deleteCampaign(id);
    }


}
