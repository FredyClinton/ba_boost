package com.yowyob.ba.service.Impl;

import com.yowyob.ba.dto.CampaignStats;
import com.yowyob.ba.entity.Interaction;
import com.yowyob.ba.enums.InteractionType;
import com.yowyob.ba.repository.AnalyticsRepository;
import com.yowyob.ba.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl  implements AnalyticsService {
    private final AnalyticsRepository analyticsRepository;

    @Override
    public Mono<CampaignStats> getCampaignStats(UUID campaignId) {
        Mono<Long> viewsMono = analyticsRepository.countInteractions(campaignId, InteractionType.VIEW.name());
        Mono<Long> clicksMono = analyticsRepository.countInteractions(campaignId, InteractionType.CLICK.name());

        // On fait les comptage en paralle
        return  Mono.zip(viewsMono, clicksMono)
                .map(tuple ->{
                    Long views = tuple.getT1();
                    Long clicks = tuple.getT2();

                    double ctr = 0.0;
                    if(views > 0) {
                        ctr = (double) clicks / views * 100;
                    }

                    return CampaignStats.builder()
                            .campaignId(campaignId)
                            .views(views)
                            .clicks(clicks)
                            .ctrs(Math.abs(ctr*100.0)/100.0) // Arrondi a 2 décimel
                            .build();
                });
    }
}
