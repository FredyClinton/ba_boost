package com.yowyob.ba.service.Impl;

import com.yowyob.ba.entity.Campaign;
import com.yowyob.ba.enums.InteractionType;
import com.yowyob.ba.service.InteractionStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ViewStrategy implements InteractionStrategy {
    @Override
    public InteractionType getSupportedType() {
        return InteractionType.VIEW;
    }

    @Override
    public Mono<Boolean> process(Campaign campaign) {
        // Ici, on pourrait ajouter un appel à Redis pour incrémenter le compteur de vues (pour le CTR)
        log.info("VIEW enregistrée pour la campagne {}", campaign.getId());
        return Mono.just(true);
    }
}
