package com.yowyob.ba.service;

import com.yowyob.ba.entity.Campaign;
import com.yowyob.ba.enums.InteractionType;
import reactor.core.publisher.Mono;

public interface InteractionStrategy {

    // Pour savoir quek type de strategie
    InteractionType getSupportedType();

    // logique metier
    Mono<Boolean> process(Campaign campaign);

}
