package edu.java.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.OffsetDateTime;
import java.util.List;

public record StackOverflowResponse(@JsonProperty("items") List<Question> items) {
    public record Question(
        @JsonProperty("title") String title,
        @JsonProperty("last_activity_date") OffsetDateTime lastActivityDate,
        @JsonProperty("answer_count") Long answerCount,
        @JsonProperty("score") Long score
    ) {
    }
}


