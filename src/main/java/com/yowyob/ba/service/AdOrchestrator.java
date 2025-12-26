package com.yowyob.ba.service;

import com.yowyob.ba.dto.AdResponse;
import com.yowyob.ba.dto.ScoreAd;
import com.yowyob.ba.dto.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdOrchestrator {
    private  final TargetingService targetingService;
    private  final ActivityGuard activityGuard;

    // Recoit un user et renvoi la meilleure pub

    public Mono<AdResponse> selectBestAd(UserContext userContext) {
        return  targetingService.findEligibleCampaigns(userContext)// M1 Ciblage
                .filterWhen(campaign ->
                    // filtre redis
                     activityGuard.isAllowed(userContext.getUserId(), campaign.getId())
                ).map(campaign -> {
                    // simule m3/m4
                    // Score = Bid Amount (enchere)
                    // Bid*pCTR
                    return new ScoreAd(campaign, campaign.getBidAmount());
                }).sort(Comparator.comparing(ScoreAd::getScore).reversed())
                .next() // prend le premier de la liste
                .flatMap(winner -> {
                    // on enregistre l'impression
                    return activityGuard.recordImpression(
                                            userContext.getUserId(),
                                            winner.getCampaign().getId()
                    ).thenReturn(AdResponse.from(winner));
                });
    }
}
