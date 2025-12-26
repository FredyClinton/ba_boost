package com.yowyob.ba.controller;

import com.yowyob.ba.dto.UserContext;
import com.yowyob.ba.entity.Campaign;
import com.yowyob.ba.entity.User;
import com.yowyob.ba.service.TargetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Slf4j
public class TestController {
    private final TargetingService targetingService;

    // Endpoint de ciblage
    @PostMapping("/targeting")
    public Flux<Campaign> testTargeting(@RequestBody UserContext user){
        return targetingService.findEligibleCampaigns(user).doOnNext(c -> log.info("Camppagne trouvé : {}", c.getId()));
    }
}
