package com.yowyob.ba.controller;

import com.yowyob.ba.dto.AdResponse;
import com.yowyob.ba.dto.UserContext;
import com.yowyob.ba.service.AdOrchestrator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/ad-decision")
@RequiredArgsConstructor
public class AdDecisionController {

    private  final AdOrchestrator   orchestrator;


    @PostMapping
    public Mono<AdResponse> getAd(@RequestBody UserContext userContext) {
        return orchestrator.selectBestAd(userContext);
    }
}
