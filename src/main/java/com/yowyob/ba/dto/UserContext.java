package com.yowyob.ba.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserContext {
    private UUID userId;
    private Integer age;
    private String city;
    private Set<String> interests;

}
