package edu.java.client;

import edu.java.entity.dto.StackOverflowResponse;
import reactor.core.publisher.Mono;

public class StackOverflowWebClient extends AbstractClient {
    public StackOverflowWebClient(String baseUrl) {
        super(baseUrl);
    }

    public Mono<StackOverflowResponse> getQuestionsInfo(String ids) {
        return client.get()
            .uri("/questions/" + ids + "?site=stackoverflow")
            .retrieve()
            .bodyToMono(StackOverflowResponse.class);
    }
}
