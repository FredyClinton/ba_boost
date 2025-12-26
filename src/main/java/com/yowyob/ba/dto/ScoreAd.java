package com.yowyob.ba.dto;

import com.yowyob.ba.entity.Campaign;
import lombok.Data;

@Data
public class ScoreAd {
    private final Campaign campaign;
    private final Double  score;
}
