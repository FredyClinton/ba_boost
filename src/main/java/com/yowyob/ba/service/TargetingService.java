package com.yowyob.ba.service;


import com.yowyob.ba.dto.UserContext;
import com.yowyob.ba.entity.Campaign;
import com.yowyob.ba.entity.User;
import reactor.core.publisher.Flux;

public interface TargetingService {


    Flux<Campaign> findEligibleCampaigns(UserContext userContext);
}
