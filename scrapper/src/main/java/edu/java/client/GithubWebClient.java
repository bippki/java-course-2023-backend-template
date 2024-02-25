package edu.java.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.java.client.GithubClient;
import edu.java.dto.GithubResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;
import java.util.Optional;

public class GithubWebClient implements GithubClient {
    @Value(value = "api.github.baseurl")
    private String baseUrl;

    private final WebClient webClient;

    public GithubWebClient() {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public GithubWebClient(String baseUrl) {
        this.baseUrl = baseUrl.isEmpty() ? this.baseUrl : baseUrl;
        this.webClient = WebClient.builder().baseUrl(this.baseUrl).build();
    }

    @Override
    public Optional<GithubResponse> fetchLatestRepositoryActivity(String repositoryName, String authorName) {
        String completedQuestionUrl = String.format("networks/%s/%s/events", authorName, repositoryName);
        return Optional.ofNullable(
            webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path(completedQuestionUrl)
                    .queryParam("per_page", 1)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .mapNotNull(this::parseResponse)
                .block()
        );
    }

    private GithubResponse parseResponse(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        try {
            List<GithubResponse> responses = objectMapper.readValue(json, new TypeReference<>() {});
            return responses.isEmpty() ? null : responses.get(0);
        } catch (Exception e) {
            return null;
        }
    }
}
