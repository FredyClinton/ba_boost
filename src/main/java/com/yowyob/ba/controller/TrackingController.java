package com.yowyob.ba.controller;

import com.yowyob.ba.enums.InteractionType;
import com.yowyob.ba.service.TrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/track")
@RequiredArgsConstructor
public class TrackingController {
    private final TrackingService trackingService;

    @PostMapping("/{campaignId}")
    public Mono<ResponseEntity<String>> track(
            @PathVariable UUID campaignId,
            @RequestParam(defaultValue = "VIEW") InteractionType type) {
        return trackingService.trackInteraction(campaignId, type)
                .map(succes -> {
                    if (succes) {
                       return ResponseEntity.ok("Interaction " + type + " save");
                    } else {
                        return ResponseEntity.badRequest().body("Echec du tracking (Campagne introuvable ou Budget épuisé)");
                    }
                });

    }
}
