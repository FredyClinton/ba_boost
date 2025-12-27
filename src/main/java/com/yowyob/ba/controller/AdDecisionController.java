package com.yowyob.ba.controller;

import com.yowyob.ba.dto.AdResponse;
import com.yowyob.ba.dto.UserContext;
import com.yowyob.ba.service.AdOrchestratorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ad-decision")
@RequiredArgsConstructor
@Tag(name = "Ad Decision Engine", description = "Moteur de décision en temps réel pour la sélection d'annonces")
public class AdDecisionController {

    private final AdOrchestratorService orchestrator;

    @Operation(
            summary = "Sélectionner la meilleure publicité",
            description = "Analyse le contexte utilisateur (âge, ville, intérêts), filtre les campagnes éligibles, " +
                    "applique le capping et exécute une enchère basée sur le score pCTR du moteur ML.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Publicité sélectionnée avec succès",
                            content = @Content(schema = @Schema(implementation = AdResponse.class))),
                    @ApiResponse(responseCode = "204", description = "Aucune publicité éligible trouvée"),
                    @ApiResponse(responseCode = "400", description = "Contexte utilisateur invalide")
            }
    )
    @PostMapping
    public Mono<AdResponse> getAd(@RequestBody UserContext userContext) {
        return orchestrator.selectBestAd(userContext);
    }
}