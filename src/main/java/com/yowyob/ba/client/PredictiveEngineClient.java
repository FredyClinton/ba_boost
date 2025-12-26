package com.yowyob.ba.client;


import com.yowyob.ba.dto.PredictionRequest;
import com.yowyob.ba.dto.UserContext;
import com.yowyob.ba.entity.Campaign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class PredictiveEngineClient {
    private  final WebClient webClient;

    public PredictiveEngineClient(@Value("${ML_ENGINE_URL}") String mlUrl) {
        this.webClient = WebClient.create(mlUrl);
    }

    public Mono<Map<UUID, Double>> getImpressionProbaility(UserContext userContext, List<Campaign> campaigns){
        PredictionRequest request = new PredictionRequest(userContext, campaigns);

        return  webClient.post()
                .uri("/predict")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<UUID, Double>>() {})
                .doOnSubscribe(s -> log.info("[JAVA -> PYTHON] Envoi demande pCTR pour User {} ({} candidats)",
                userContext.getUserId(), campaigns.size()))
                .doOnNext(response -> log.info("[PYTHON -> JAVA] Scores reçus : {}", response))
                .doOnError(e -> log.error("[ERREUR ML] Impossible de contacter le moteur Python : {}", e.getMessage()));

    }
}
