package edu.java.entity.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowResponse(@JsonProperty("items") List<Question> items) {
    public record Question(
        @JsonProperty("title") String title,
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate
    ) {
    }
}


