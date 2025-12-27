package com.yowyob.ba.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yowyob.ba.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("publications")
public class Publication {

    @Id
    private UUID id;

    private String title;

    private String description;

    @Column("content_type")
    private ContentType contentType;

    @Column("media_urls")
    private String[] mediaUrls;


    @Version
    @JsonIgnore
    private  Long version;
}
