package com.yowyob.ba.service.Impl;

import com.yowyob.ba.enums.InteractionType;
import com.yowyob.ba.repository.CampaignRepository;
import com.yowyob.ba.service.CampaignService;
import com.yowyob.ba.service.InteractionStrategy;
import com.yowyob.ba.service.TrackingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrackingSeriveImpl  implements TrackingService {

    private final CampaignRepository campaignRepository;
    private  final Map<InteractionType, InteractionStrategy> strategies;

    public TrackingSeriveImpl(CampaignRepository campaignRepository, List<InteractionStrategy> strategyList) {
        this.campaignRepository = campaignRepository;
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
                .defaultIfEmpty(false);
    }
}
