package com.yowyob.ba.service.Impl;

import com.yowyob.ba.entity.Campaign;
import com.yowyob.ba.repository.CampaignRepository;
import com.yowyob.ba.service.CampaignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CampaignServiceImpl implements CampaignService {
    private final CampaignRepository campaignRepository;

    @Override
    public Mono<Campaign> createCampaign(Campaign campaign) {
        // Si l'uuid n'est pas fourni, on génère un
        if(campaign.getId() == null){
            campaign.setId(UUID.randomUUID());
        }
        return campaignRepository.save(campaign)
                .doOnNext(c -> {
                    assert c != null;
                    log.info("Campagne crée {}", c.getId());
                });
    }

    @Override
    public Mono<Campaign> getCampaignById(UUID id) {
        return campaignRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Campagne introuvable avec l'ID : " + id)));
    }

    @Override
    public Flux<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    @Override
    public Mono<Campaign> updateCampaign(UUID id, Campaign campaignDetails) {
        return campaignRepository.findById(id)
                .flatMap(existingCampaign -> {
                    // Mise à jour des champs (on ne touche pas à l'ID ni au profil annonceur pour l'instant)
                    existingCampaign.setStatus(campaignDetails.getStatus());
                    existingCampaign.setMinAge(campaignDetails.getMinAge());
                    existingCampaign.setMaxAge(campaignDetails.getMaxAge());
                    existingCampaign.setCity(campaignDetails.getCity());
                    existingCampaign.setCountry(campaignDetails.getCountry());
                    existingCampaign.setTargetInterests(campaignDetails.getTargetInterests());
                    existingCampaign.setBudgetRemaining(campaignDetails.getBudgetRemaining());
                    existingCampaign.setBidAmount(campaignDetails.getBidAmount());

                    return campaignRepository.save(existingCampaign);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Impossible de mettre à jour : Campagne introuvable")));
    }

    @Override
    public Mono<Void> deleteCampaign(UUID id) {
        return campaignRepository.deleteById(id)
                .doOnSuccess(unused -> log.info("Campagne supprimée : {}", id));
    }
}
