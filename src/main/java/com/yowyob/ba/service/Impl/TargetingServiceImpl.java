package com.yowyob.ba.service.Impl;

import com.yowyob.ba.dto.UserContext;
import com.yowyob.ba.entity.Campaign;
import com.yowyob.ba.repository.CampaignRepository;
import com.yowyob.ba.service.TargetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

// Module 1
@Service
@RequiredArgsConstructor
@Slf4j
public class TargetingServiceImpl implements TargetingService {
    private final CampaignRepository campaignRepository;

    @Override
    public Flux<Campaign> findEligibleCampaigns(UserContext userContext) {
        log.info("M1: Recherche de campagnes pour l'utilisateur {} (Age: {}, Ville: {})",
                userContext.getUserId(), userContext.getAge(), userContext.getCity());

        Flux<Campaign> fluxCampaigns = campaignRepository.findEligibleCampaigns(
                 userContext.getAge(), userContext.getCity()
        );

        // On fait un second tri pour garder ceux qui peuvent avoir de l'interêt pour le user
        return  fluxCampaigns.filter( campaign -> {
            if (campaign.getTargetInterests() == null || campaign.getTargetInterests().isEmpty()){
                return  true;
            }

            if(userContext.getInterests() == null){
                return false;
            }
            // on garde la campagne si user a au moins un interet commun avec elle
            return userContext.getInterests().stream()
                    .anyMatch(interest -> campaign.getTargetInterests().contains(interest));

        }).doOnNext( campaign -> log.debug("Campagne éligible trouvé: {}", campaign.getId()));
    }

}
