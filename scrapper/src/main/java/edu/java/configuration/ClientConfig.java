package edu.java.configuration;

import edu.java.client.GithubWebClient;
import edu.java.client.StackOverflowWebClient;
import edu.java.client.BotClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("unused")
@Configuration
@RequiredArgsConstructor
public class ClientConfig {
    private final ApplicationConfig config;

    @Bean
    public StackOverflowWebClient stackOverflowWebClient() {
        return new StackOverflowWebClient(config.apiLink().stackOverflow());
    }

    @Bean
    public GithubWebClient githubClient() {
        return new GithubWebClient(config.apiLink().gitHub());
    }

    @Bean
    public BotClient botClient() {
        return new BotClient(config.apiLink().bot());
    }
}
