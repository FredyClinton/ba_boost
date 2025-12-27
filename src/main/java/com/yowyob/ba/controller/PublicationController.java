package com.yowyob.ba.controller;

import com.yowyob.ba.entity.Publication;
import com.yowyob.ba.service.PublicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/publications")
@RequiredArgsConstructor
public class PublicationController {
    private final PublicationService publicationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Publication> createPublication(@RequestBody Publication publication) {
        return publicationService.createPublication(publication);
    }

    @GetMapping
    public Flux<Publication> getAllPublications() {
        return publicationService.getAllPublications();
    }

    @GetMapping("/{id}")
    public Mono<Publication> getPublication(@PathVariable UUID id) {
        return publicationService.getPublicationById(id);
    }

    @DeleteMapping
    public Mono<Void> deletePublication(@PathVariable UUID id) {
        return publicationService.deletePublication(id);
    }

    @PutMapping("/{id}")
    public Mono<Publication> updatePublication(@PathVariable UUID id, @RequestBody Publication publication) {
      return   this.publicationService.updatePublication(id, publication);
    }
}
