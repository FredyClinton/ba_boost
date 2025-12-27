package com.yowyob.ba.service.Impl;

import com.yowyob.ba.entity.Publication;
import com.yowyob.ba.repository.PublicationRepository;
import com.yowyob.ba.service.PublicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublicationServiceImpl implements PublicationService {
    private final PublicationRepository publicationRepository;

    @Override
    public Mono<Publication> createPublication(Publication publication) {
        if (publication.getId() == null) {
            publication.setId(UUID.randomUUID());
        }
        return this.publicationRepository.save(publication)
                .doOnNext(p ->{
                    assert p != null;
                    log.info("Publication created with id {}", p.getId());
                })
                ;
    }

    @Override
    public Flux<Publication> getAllPublications() {
        return this.publicationRepository.findAll();
    }

    @Override
    public Mono<Publication> getPublicationById(UUID id) {
        return this.publicationRepository.findById(id).switchIfEmpty(Mono.error(
                new RuntimeException("Publication not found avec l'ID: "+ id)));
    }

    @Override
    public Mono<Void> deletePublication(UUID id) {
        return this.publicationRepository.deleteById(id)
                .doOnSuccess(unused -> log.info("Publication {} delete", id))
                ;
    }

    @Override
    public Mono<Publication> updatePublication(UUID id, Publication publication) {
        return this.publicationRepository.findById(id)
                .flatMap(existingPublication ->{
                    existingPublication.setDescription(publication.getDescription());
                    existingPublication.setTitle(publication.getTitle());
                    existingPublication.setMediaUrls(publication.getMediaUrls());
                    // On ne touche pas au centent type pour le moment
                    return this.publicationRepository.save(existingPublication);

                })
                ;
    }
}
