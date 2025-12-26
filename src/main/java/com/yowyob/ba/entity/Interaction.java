package com.yowyob.ba.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("interactions")
public class Interaction {

    @Id
    private UUID id;

    private UUID campaignId;
    private  UUID userId;
    private String interactionType;
    private Instant timestamp;

    @Version
    private Long version;

}
