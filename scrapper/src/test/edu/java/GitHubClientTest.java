package edu.java;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.client.GithubWebClient;
import edu.java.entity.dto.GithubResponse;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GitHubClientTest {
    private static final String repositoryName = "java-course-2023-backend-template";
    private static final String authorName = "bippki";

    @RegisterExtension
    public static final WireMockExtension WIRE_MOCK_SERVER = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().dynamicPort())
        .build();

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("scrapper.api-link.github", WIRE_MOCK_SERVER::baseUrl);
    }

    @Autowired
    private GithubWebClient client;


    @Test
    public void testGetUserRepositoryForExistingRepository() {
        // Given
        final String repositoryPath = authorName +  "/" + repositoryName;
        final OffsetDateTime repositoryCreatedAt = OffsetDateTime.of(2023, 6, 27, 21, 10, 31, 0, ZoneOffset.UTC);
        WIRE_MOCK_SERVER.stubFor(WireMock.get("/repos/" + repositoryPath)
            .willReturn(WireMock.ok()
                .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(String.format("{\"full_name\": \"%s\", \"updated_at\": \"2023-06-27T21:10:31Z\"}", repositoryPath))));

        // When
        GithubResponse response = client.getUserRepository(repositoryPath).block();

        // Then
        assertNotNull(response);
        assertEquals(repositoryPath, response.owner().login() + "/" + response.name());
        assertEquals(repositoryCreatedAt, response.createdAt());
    }

    @Test
    public void testGetUserRepositoryForNonExistingRepository() {
        // Given
        final String repositoryPath = "bippki/Spooky";
        WIRE_MOCK_SERVER.stubFor(WireMock.get("/repos/" + repositoryPath)
            .willReturn(WireMock.notFound()
                .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .withBody(String.format("{\"full_name\": \"%s\", \"created_at\": \"2023-06-27T21:10:31Z\", \"owner\": {\"login\": \"%s\", \"id\": \"12345\"}, \"name\": \"%s\"}", repositoryPath, authorName, repositoryName))));

        // When, Then
        assertThrows(WebClientResponseException.class, () -> client.getUserRepository(repositoryPath).block());
    }
}
