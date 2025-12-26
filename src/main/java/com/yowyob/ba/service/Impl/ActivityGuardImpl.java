package com.yowyob.ba.service.Impl;

import com.yowyob.ba.service.ActivityGuard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

// Module 2
@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityGuardImpl implements ActivityGuard {
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    // Limite : Pas plus de 3 fois la meme pub pendant 24h
    private static  final  int MAX_IMPRESSIONS = 3;
    private  static  final Duration TTL = Duration.ofHours(24);

    private String getKey(UUID userId, UUID campaignID){
        return "capping:" + userId + ":" + campaignID;
    }

    @Override
    public Mono<Boolean> isAllowed(UUID userId, UUID campaignID) {
        String key = getKey(userId, campaignID);
        return  reactiveRedisTemplate.opsForValue()
                .get(key)
                .map(count -> {
                    int currentCount = Integer.parseInt(count);
                    boolean allowed = currentCount < MAX_IMPRESSIONS;
                    if(!allowed){
                        log.debug("M2: Campagne {} bloque pour user {} (Trop de vues: {})",
                                campaignID, userId, currentCount);
                    }
                    return allowed;
                })
                .defaultIfEmpty(true);
    }

    @Override
    public Mono<Void> recordImpression(UUID userId, UUID campaignID) {
        String key = getKey(userId, campaignID);
        return  reactiveRedisTemplate.opsForValue()
                .increment(key)
                .flatMap(count -> {
                    // si c'est la premiere fois on met un temps d'expiration
                    if (count == 1){
                        return  reactiveRedisTemplate.expire(key, TTL);
                    }
                    return Mono.just(true);
                }).then(); // le resultat n'est pas utilise
    }
}
