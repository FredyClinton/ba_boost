package com.yowyob.ba.service;


import com.yowyob.ba.enums.InteractionType;
import reactor.core.publisher.Mono;

import java.util.UUID;

public  interface TrackingService {

    // Methode generique
    Mono<Boolean> trackInteraction(UUID campaignId, InteractionType type);
}