package edu.java.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@JsonIgnoreProperties(ignoreUnknown = true)
public record StackOverflowResponse(
    @JsonProperty("question_id") Long questionId,
    @JsonProperty("answer_id") Long answerId,
    @JsonProperty("owner")Owner owner,

    @JsonProperty("body") String body,
    @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate
) {
    public record Owner(
        @JsonProperty("display_name") String displayName
    ) {}
}


