package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@JsonIgnoreProperties(ignoreUnknown = true)
public record GithubResponse(
    @JsonProperty("id") Long id,
    @JsonProperty("type") String type,
    @JsonProperty("actor")Author author,
    @JsonProperty("repo")Repo repo,
    @JsonProperty("created_at") OffsetDateTime createdAt
) {
    public record Author(
        @JsonProperty("display_login") String authorName
    ) {}

    public record Repo(
        @JsonProperty("name") String repositoryName
    ) {}
}
