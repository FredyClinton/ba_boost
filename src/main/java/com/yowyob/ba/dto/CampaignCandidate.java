package com.yowyob.ba.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampaignCandidate {
    private UUID id;
    private Set<String> targetInterests;
}
