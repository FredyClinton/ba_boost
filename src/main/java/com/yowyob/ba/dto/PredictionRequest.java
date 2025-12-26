package com.yowyob.ba.dto;

import com.yowyob.ba.entity.Campaign;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
//@AllArgsConstructor
@NoArgsConstructor
public class PredictionRequest {
    private  UserContext userContext;
    private  List<CampaignCandidate> candidates;

    public PredictionRequest(UserContext userContext, List<Campaign> campaigns){
        this.userContext = userContext;
        this.candidates = campaigns.stream()
                .map(c -> new CampaignCandidate(c.getId(), c.getTargetInterests()))
                .collect(Collectors.toList());
    }

}
