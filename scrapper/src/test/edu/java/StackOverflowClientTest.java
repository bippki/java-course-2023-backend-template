package edu.java;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Objects;
import edu.java.client.StackOverflowWebClient;
import edu.java.entity.dto.StackOverflowResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StackOverflowClientTest {
    @RegisterExtension
    public static final WireMockExtension WIRE_MOCK_SERVER = WireMockExtension.newInstance()
        .options(WireMockConfiguration.wireMockConfig().dynamicPort())
        .build();

    @DynamicPropertySource
    private static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.api-link.stack-overflow", WIRE_MOCK_SERVER::baseUrl);
    }

    @Autowired
    private StackOverflowWebClient client;

    @Test
    public void testQuestionRetrievalForExistingQuestion() {
        final String questionIds = "47176744";
        final int epochSecond = 1511021257;
        final OffsetDateTime lastActivityDate =
            OffsetDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.of("Z"));
        final String expectedTitle = "What is your funny meme about programming?";
        mockQuestionResponse(questionIds, epochSecond, expectedTitle);
        StackOverflowResponse response = client.getQuestionsInfo(questionIds).block();
        Objects.requireNonNull(response);

    }

    @Test
    public void testQuestionRetrievalForNonExistingQuestion() {
        final String questionIds = "1";
        mockEmptyQuestionResponse(questionIds);
        StackOverflowResponse response = client.getQuestionsInfo(questionIds).block();

        Objects.requireNonNull(response);
        assertTrue(response.items().isEmpty());
    }

    private void mockQuestionResponse(String questionIds, int epochSecond, String title) {
        WIRE_MOCK_SERVER.stubFor(WireMock.get("/questions/" + questionIds + "?site=stackoverflow")
            .willReturn(WireMock.ok()
                .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "items": [
                            {
                                "last_activity_date": %d,
                                "title": "%s"
                            }
                        ]
                    }
                    """.formatted(epochSecond, title))));
    }

    private void mockEmptyQuestionResponse(String questionIds) {
        WIRE_MOCK_SERVER.stubFor(WireMock.get("/questions/" + questionIds + "?site=stackoverflow")
            .willReturn(WireMock.ok()
                .withHeader("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                    {
                        "items": []
                    }
                    """)));
    }
}
