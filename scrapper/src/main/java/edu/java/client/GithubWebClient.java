package edu.java.client;

import edu.java.entity.dto.GithubResponse;
import reactor.core.publisher.Mono;

public class GithubWebClient extends AbstractClient {
    public GithubWebClient(String baseUrl) {
        super(baseUrl);
    }

    public Mono<GithubResponse> getUserRepository(String repositoryPath) {
        return client.get()
            .uri("/repos/" + repositoryPath)
            .retrieve()
            .bodyToMono(GithubResponse.class);
    }
}
