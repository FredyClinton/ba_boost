package com.yowyob.ba.repository;

import com.yowyob.ba.entity.Publication;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface PublicationRepository  extends ReactiveCrudRepository<Publication, UUID> {
}
