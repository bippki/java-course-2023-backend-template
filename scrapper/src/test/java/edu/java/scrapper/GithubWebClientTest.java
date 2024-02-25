package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.client.GithubClient;
import edu.java.client.GithubWebClient;
import edu.java.dto.GithubResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

public class GithubWebClientTest {

    private WireMockServer wireMockServer;
    private GithubClient githubClient;

    // TODO Поменяй
    private static final String repositoryName = "test";
    private static final String authorName = "bippki";

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer();
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());

        String baseUrl = "http://localhost:" + wireMockServer.port();
        githubClient = new GithubWebClient(baseUrl);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void testFetchLatestActivity() {
        String responseBody = "[{\"id\":123,\"type\":\"PushEvent\",\"actor\":{\"display_login\":\"" + authorName + "\"}," +
            "\"repo\":{\"name\":\"" + repositoryName + "\"},\"created_at\":1644759591}]";

        wireMockServer.stubFor(get(urlEqualTo(String.format("/networks/%s/%s/events?per_page=1",
            authorName, repositoryName)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        GithubResponse response = githubClient.fetchLatestRepositoryActivity(repositoryName, authorName).get();

        assertNotNull(response);
        assertEquals(123L, response.id());
        assertEquals("PushEvent", response.type());
        assertEquals(authorName, response.author().authorName());
        assertEquals(repositoryName, response.repo().repositoryName());
        assertEquals(OffsetDateTime.ofInstant(Instant.ofEpochSecond(1644759591), ZoneOffset.UTC),
            response.createdAt());
    }

    @Test
    public void testFetchLatestActivityEmpty() {
        String responseBody = "[]";
        wireMockServer.stubFor(get(urlEqualTo(String.format("/networks/%s/%s/events?per_page=1",
            authorName, repositoryName)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        Optional<GithubResponse> response = githubClient.fetchLatestRepositoryActivity(repositoryName, authorName);

        assertFalse(response.isPresent());
    }

    @Test
    public void testFetchLatestActivityInvalidJson() {
        String responseBody = "incorrect";

        wireMockServer.stubFor(get(urlEqualTo(String.format("/networks/%s/%s/events?per_page=1",
            authorName, repositoryName)))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));

        Optional<GithubResponse> response = githubClient.fetchLatestRepositoryActivity(repositoryName, authorName);
        assertFalse(response.isPresent());
    }
}
