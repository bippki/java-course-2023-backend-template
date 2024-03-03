package edu.java.client;

import edu.java.entity.dto.bot.ApiErrorResponse;
import edu.java.entity.dto.bot.LinkUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import edu.java.exception.ApiErrorResponseException;
import reactor.core.publisher.Mono;

public class BotClient extends AbstractClient {
    public BotClient(String baseUrl) {
        super(baseUrl);
    }

    public Mono<ResponseEntity<Void>> sendUpdate(LinkUpdateRequest request) {
        return client.post()
            .uri("/updates")
            .bodyValue(request)
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response.bodyToMono(ApiErrorResponse.class).map(ApiErrorResponseException::new)
            )
            .toBodilessEntity();
    }
}
