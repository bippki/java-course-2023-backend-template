package edu.java.client;

import org.springframework.web.reactive.function.client.WebClient;

public abstract class AbstractClient {
    protected final WebClient client;

    public AbstractClient(String baseUrl) {
        client = WebClient.create(baseUrl);
    }
}
