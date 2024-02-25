package edu.java.configuration;

import edu.java.client.GithubWebClient;
import edu.java.client.StackOverflowWebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class ClientConfiguration {
    private final String baseUrlStackoverflow;
    private final String baseUrlGithub;

    public ClientConfiguration(
        @Value("${api.stackoverflow.baseurl}") String baseUrlStackoverflow,
        @Value("${api.github.baseurl}") String baseUrlGithub
    ) {
        this.baseUrlStackoverflow = baseUrlStackoverflow;
        this.baseUrlGithub = baseUrlGithub;
    }

    @Bean
    public StackOverflowWebClient stackOverflowWebClient() {
        return new StackOverflowWebClient(baseUrlStackoverflow);
    }

    @Bean
    public GithubWebClient githubClient() {
        return new GithubWebClient(baseUrlGithub);
    }
}
