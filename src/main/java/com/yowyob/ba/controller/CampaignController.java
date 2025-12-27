package com.yowyob.ba.controller;

import com.yowyob.ba.entity.Campaign;
import com.yowyob.ba.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/campaigns")
@RequiredArgsConstructor
@Tag(name = "Campaign Management", description = "Gestion du cycle de vie des campagnes publicitaires")
public class CampaignController {
    private final CampaignService campaignService;

    @Operation(summary = "Lister toutes les campagnes", description = "Récupère la liste complète des campagnes sans filtre.")
    @GetMapping
    public Flux<Campaign> getCampaigns() {
        return campaignService.getAllCampaigns();
    }

    @Operation(summary = "Obtenir une campagne par ID")
    @GetMapping("/{id}")
    public Mono<Campaign> getCampaignById(@PathVariable UUID id) {
        return campaignService.getCampaignById(id);
    }

    @Operation(summary = "Créer une nouvelle campagne", responses = {
            @ApiResponse(responseCode = "201", description = "Campagne créée avec succès")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Campaign> createCampaign(@RequestBody Campaign campaign) {
        return campaignService.createCampaign(campaign);
    }

    @Operation(summary = "Mettre à jour une campagne existante")
    @PutMapping("/{id}")
    public Mono<Campaign> updateCampaign(@PathVariable UUID id, @RequestBody Campaign campaign) {
        return campaignService.updateCampaign(id, campaign);
    }

    @Operation(summary = "Supprimer une campagne")
    @DeleteMapping("/{id}")
    public Mono<Void> deleteCampaign(@PathVariable UUID id) {
        return campaignService.deleteCampaign(id);
    }
}