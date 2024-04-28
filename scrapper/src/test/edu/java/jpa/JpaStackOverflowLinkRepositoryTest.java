package edu.java.jpa;

import edu.java.IntegrationTest;
import edu.java.entity.StackOverflowLink;
import edu.java.repository.jpa.JpaStackOverflowLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
public class JpaStackOverflowLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JpaStackOverflowLinkRepository stackOverflowLinkRepository;

    @DynamicPropertySource
    public static void setJpaAccessType(DynamicPropertyRegistry registry) {
        registry.add("scrapper.database-access-type", () -> "jpa");
    }

    private StackOverflowLink stackOverflowLink;

    @BeforeEach
    public void setup() {
        stackOverflowLink = new StackOverflowLink();
        stackOverflowLink.setId(145L);
        stackOverflowLink.setAnswerCount(1L);
        stackOverflowLink.setScore(12L);
        stackOverflowLink.setUrl(URI.create("https://test.com"));
        stackOverflowLink.setLastUpdatedAt(OffsetDateTime.now());
    }

    @Test
    @Rollback
    public void testNonExistingStackOverflowLinkThrowsException() {
        final long nonExistingLinkId = Long.MAX_VALUE;
        assertEquals(Optional.empty(), stackOverflowLinkRepository.findById(nonExistingLinkId));
    }

    @Test
    @Rollback
    public void testAddStackOverflowLink() {
        StackOverflowLink savedStackOverflowLink = stackOverflowLinkRepository.save(stackOverflowLink);

        assertEquals(stackOverflowLink.getId(), savedStackOverflowLink.getId());
        assertEquals(stackOverflowLink.getAnswerCount(), savedStackOverflowLink.getAnswerCount());
        assertEquals(stackOverflowLink.getScore(), savedStackOverflowLink.getScore());
    }

    @Test
    @Rollback
    public void testUpdateStackOverflowLink() {
        StackOverflowLink savedStackOverflowLink = stackOverflowLinkRepository.save(stackOverflowLink);
        savedStackOverflowLink.setScore(1L);

        stackOverflowLinkRepository.save(savedStackOverflowLink);

        assertEquals(stackOverflowLink.getId(), savedStackOverflowLink.getId());
        assertEquals(stackOverflowLink.getAnswerCount(), savedStackOverflowLink.getAnswerCount());
        assertNotEquals(stackOverflowLink.getScore(), savedStackOverflowLink.getScore());
    }
}
