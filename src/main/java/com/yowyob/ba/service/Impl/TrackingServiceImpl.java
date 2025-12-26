package com.yowyob.ba.service.Impl;

import com.yowyob.ba.entity.Interaction;
import com.yowyob.ba.enums.InteractionType;
import com.yowyob.ba.repository.CampaignRepository;
import com.yowyob.ba.repository.InteractionRepository;
import com.yowyob.ba.service.InteractionStrategy;
import com.yowyob.ba.service.TrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrackingServiceImpl implements TrackingService {

    private final CampaignRepository campaignRepository;
    private  final Map<InteractionType, InteractionStrategy> strategies;
    private  final InteractionRepository interactionRepository;

    public TrackingServiceImpl(CampaignRepository campaignRepository,
                               List<InteractionStrategy> strategyList,
                               InteractionRepository interactionRepository) {
        this.campaignRepository = campaignRepository;
        this.interactionRepository = interactionRepository;
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(InteractionStrategy::getSupportedType, Function.identity()));
    }

    @Override
    public Mono<Boolean> trackInteraction(UUID campaignId, InteractionType type) {
        // 1. Trouver la strategies
        InteractionStrategy strategy = strategies.get(type);

        if (strategy == null) {
            log.warn("Type d'interaction inconnu : {}", type);
            return Mono.just(false);
        }

        // 2. executer la strategie
        return campaignRepository.findById(campaignId)
                .flatMap(strategy::process)
                .flatMap(succes ->{
                    if(succes) {
                        return saveLog(campaignId, type).thenReturn(true);
                    }
                    return Mono.just(false);
                })
                .defaultIfEmpty(false);
    }

    private Mono<Interaction> saveLog(UUID campaignId, InteractionType type) {
        // On génère un fake user ID
        // On va le remplacer plus tard
        UUID anonymousId = UUID.randomUUID();

        Interaction interaction = Interaction.builder()
                .id(UUID.randomUUID())
                .campaignId(campaignId)
                .userId(anonymousId)
                .interactionType(type.name())
                .timestamp(Instant.now())
                .version(null) // Pour forcer l'insert
                .build();

        return interactionRepository.save(interaction)
                .doOnSuccess(i -> log.info("Interaction {} sauvegarde en base", type));
    }
}
