package com.yowyob.ba.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CampaignStats {
    private UUID campaignId;
    private Long views;
    private Long clicks;
    private Double ctrs; // pourcentages
}
