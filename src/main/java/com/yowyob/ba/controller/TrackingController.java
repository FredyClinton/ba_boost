package com.yowyob.ba.controller;

import com.yowyob.ba.enums.InteractionType;
import com.yowyob.ba.service.TrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/track")
@RequiredArgsConstructor
@Tag(name = "Tracking", description = "Enregistrement des interactions utilisateurs et facturation")
public class TrackingController {
    private final TrackingService trackingService;

    @Operation(
            summary = "Enregistrer une interaction utilisateur",
            description = "Traite une vue ou un clic. En cas de clic, déduit automatiquement le montant de l'enchère du budget restant de la campagne.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Interaction enregistrée et facturée"),
                    @ApiResponse(responseCode = "400", description = "Échec du tracking (Budget épuisé ou ID invalide)")
            }
    )
    @PostMapping("/{campaignId}")
    public Mono<ResponseEntity<String>> track(
            @Parameter(description = "ID de la campagne ciblée") @PathVariable UUID campaignId,
            @Parameter(description = "Type d'interaction (VIEW, CLICK, LIKE)") @RequestParam(defaultValue = "VIEW") InteractionType type) {
        return trackingService.trackInteraction(campaignId, type)
                .map(succes -> {
                    if (succes) {
                        return ResponseEntity.ok("Interaction " + type + " enregistrée avec succès");
                    } else {
                        return ResponseEntity.badRequest().body("Échec du tracking : Campagne introuvable ou Budget épuisé");
                    }
                });
    }
}