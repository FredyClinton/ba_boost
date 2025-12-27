package com.yowyob.ba.controller;

import com.yowyob.ba.dto.CampaignStats;
import com.yowyob.ba.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Analytics", description = "Statistiques de performance des campagnes")
public class AnalyticsController {
    private final AnalyticsService analyticsService;

    @Operation(
            summary = "Récupérer les statistiques d'une campagne",
            description = "Calcule le nombre de vues, de clics et le CTR (Click-Through Rate) actuel pour une campagne donnée.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Statistiques récupérées"),
                    @ApiResponse(responseCode = "404", description = "Campagne non trouvée")
            }
    )
    @GetMapping("/{campaignId}")
    public Mono<CampaignStats> getStats(
            @Parameter(description = "ID unique de la campagne") @PathVariable UUID campaignId) {
        return analyticsService.getCampaignStats(campaignId);
    }
}