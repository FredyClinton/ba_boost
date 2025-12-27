package com.yowyob.ba.service;

import com.yowyob.ba.entity.Publication;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PublicationService {
    Mono<Publication> createPublication(Publication publication);
    Flux<Publication> getAllPublications();
    Mono<Publication> getPublicationById(UUID id);
    Mono<Void> deletePublication(UUID id);
    Mono<Publication> updatePublication(UUID id, Publication publication);
}
