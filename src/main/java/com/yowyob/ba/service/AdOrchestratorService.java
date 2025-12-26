package com.yowyob.ba.service;

import com.yowyob.ba.client.PredictiveEngineClient;
import com.yowyob.ba.dto.AdResponse;
import com.yowyob.ba.dto.ScoreAd;
import com.yowyob.ba.dto.UserContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdOrchestratorService {

    private final TargetingService targetingService;
    private final ActivityGuard activityGuard;
    private final PredictiveEngineClient predictiveEngineClient; // Injection du client

    public Mono<AdResponse> selectBestAd(UserContext userContext) {
        return targetingService.findEligibleCampaigns(userContext)
                .filterWhen(c -> activityGuard.isAllowed(userContext.getUserId(), c.getId()))
                .collectList() // On rassemble tout dans une liste pour l'envoyer en bloc à Python
                .flatMap(candidates -> {
                    if (candidates.isEmpty()) return Mono.empty();

                    // Appel au Module 3 (Python)
                    return predictiveEngineClient.getImpressionProbaility(userContext, candidates)
                            .map(scores -> {
                                // Module 4 : Enchères (Bid * pCTR)
                                return candidates.stream()
                                        .map(c -> {
                                            Double pCtr = scores.getOrDefault(c.getId(), 0.01);
                                            Double finalScore = c.getBidAmount() * pCtr;
                                            log.debug("Campagne {} : Bid={} * pCTR={} = Score={}", c.getId(), c.getBidAmount(), pCtr, finalScore);
                                            return new ScoreAd(c, finalScore);
                                        })
                                        .max(Comparator.comparing(ScoreAd::getScore))
                                        .orElseThrow();
                            });
                })
                .flatMap(winner ->
                        activityGuard.recordImpression(userContext.getUserId(), winner.getCampaign().getId())
                                .thenReturn(AdResponse.from(winner))
                );
    }
}