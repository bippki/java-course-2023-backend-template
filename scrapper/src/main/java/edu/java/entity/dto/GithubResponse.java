package edu.java.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubResponse(
    @JsonProperty("id") Long id,
    @JsonProperty("type") String type,
    @JsonProperty("name")String name,
    @JsonProperty("created_at") OffsetDateTime createdAt,

    @JsonProperty("owner")Owner owner
) {
    public record Owner(
        @JsonProperty("login") String login,
        @JsonProperty("id") String id
    ) {}
}
