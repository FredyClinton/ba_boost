package com.yowyob.ba.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdResponse {

    private UUID campaignId;
    private Double finalScore;
    private String contentUrl;

    public  static  AdResponse from(ScoreAd scoreAd){
        return  AdResponse.builder()
                .campaignId(scoreAd.getCampaign().getId())
                .finalScore(scoreAd.getScore())
                .build();
    }
}
