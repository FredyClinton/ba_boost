package com.yowyob.ba.service.Impl;

import com.yowyob.ba.entity.Campaign;
import com.yowyob.ba.enums.InteractionType;
import com.yowyob.ba.repository.CampaignRepository;
import com.yowyob.ba.service.InteractionStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClickStrategy implements InteractionStrategy {
    private  final CampaignRepository CampaignRepository;
    private final CampaignRepository campaignRepository;

    @Override
    public InteractionType getSupportedType() {
        return InteractionType.CLICK;
    }

    @Override
    public Mono<Boolean> process(Campaign campaign) {
        Double cost = campaign.getBidAmount();

        return campaignRepository.deductBudget(
                campaign.getId(), cost
        ).map(
                rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        log.info("Click facture: -{} sur la campagne {}", cost, campaign.getId());
                        return  true;
                    }else {
                        log.warn("Budget epuise : {} ", campaign.getId());
                        return false;
                    }
                }
        );
    }
}
