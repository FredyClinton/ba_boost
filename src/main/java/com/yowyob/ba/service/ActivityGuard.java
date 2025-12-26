package com.yowyob.ba.service;

import reactor.core.publisher.Mono;

import java.util.UUID;

public interface  ActivityGuard {
    // Verifie si l'utilisateur n'as pas depassé la limite pour cette campagne
    Mono<Boolean> isAllowed(UUID userId, UUID campaingId);

    // Enregistre une vue (increment le compteur)
    Mono<Void> recordImpression(UUID userId, UUID campaingId);
}
