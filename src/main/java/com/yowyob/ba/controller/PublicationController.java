package com.yowyob.ba.controller;

import com.yowyob.ba.entity.Publication;
import com.yowyob.ba.service.PublicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/publications")
@RequiredArgsConstructor
@Tag(name = "Publication Management", description = "Gestion de la médiathèque et des contenus publicitaires")
public class PublicationController {
    private final PublicationService publicationService;

    @Operation(summary = "Créer une publication (Média)", description = "Ajoute un contenu (Image, Vidéo, Story) à la bibliothèque.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Publication> createPublication(@RequestBody Publication publication) {
        return publicationService.createPublication(publication);
    }

    @Operation(summary = "Lister toutes les publications")
    @GetMapping
    public Flux<Publication> getAllPublications() {
        return publicationService.getAllPublications();
    }

    @Operation(summary = "Récupérer une publication par ID")
    @GetMapping("/{id}")
    public Mono<Publication> getPublication(@PathVariable UUID id) {
        return publicationService.getPublicationById(id);
    }

    @Operation(summary = "Mettre à jour une publication")
    @PutMapping("/{id}")
    public Mono<Publication> updatePublication(@PathVariable UUID id, @RequestBody Publication publication) {
        return publicationService.updatePublication(id, publication);
    }

    @Operation(summary = "Supprimer une publication")
    @DeleteMapping("/{id}")
    public Mono<Void> deletePublication(@PathVariable UUID id) {
        return publicationService.deletePublication(id);
    }
}