package com.yowyob.ba.repository;

import com.yowyob.ba.entity.Interaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface InteractionRepository extends ReactiveCrudRepository<Interaction, UUID> {
}
