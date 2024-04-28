package edu.java.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubResponse(
    @JsonProperty("id") Long id,
    @JsonProperty("type") String type,
    @JsonProperty("name")String name,
    @JsonProperty("full_name") String fullName,
    @JsonProperty("created_at") OffsetDateTime createdAt,
    @JsonProperty("updated_at") OffsetDateTime updatedAt,
    @JsonProperty("owner")Owner owner,
    @JsonProperty("default_branch") String defaultBranch,
    @JsonProperty("forks_count") Long forksCount
) {
    public record Owner(
        @JsonProperty("login") String login,
        @JsonProperty("id") String id
    ) {}
}
