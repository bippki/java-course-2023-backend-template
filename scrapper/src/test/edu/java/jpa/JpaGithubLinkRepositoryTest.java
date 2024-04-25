package edu.java.jpa;

import edu.java.IntegrationTest;
import edu.java.entity.GithubLink;
import edu.java.repository.jpa.JpaGitHubLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@Transactional
public class JpaGithubLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JpaGitHubLinkRepository githubLinkRepository;

    @DynamicPropertySource
    public static void setJpaAccessType(DynamicPropertyRegistry registry) {
        registry.add("scrapper.database-access-type", () -> "jpa");
    }

    private GithubLink createGithubLink(
        long id,
        String defaultBranch,
        long forksCount,
        URI url,
        OffsetDateTime lastUpdatedAt
    ) {
        GithubLink githubLink = new GithubLink();
        githubLink.setId(id);
        githubLink.setDefaultBranch(defaultBranch);
        githubLink.setForksCount(forksCount);
        githubLink.setUrl(url);
        githubLink.setLastUpdatedAt(lastUpdatedAt);
        ;
        return githubLink;
    }

    @Test
    public void testGetNonExistingGithubLinkThrowsException() {
        final long nonExistingLinkId = Long.MAX_VALUE;
        assertEquals(Optional.empty(), githubLinkRepository.findById(nonExistingLinkId));
    }

    @Test
    public void testAddExistingLink() {
        GithubLink githubLink =
            createGithubLink(3124L, "master", 1L, URI.create("https://test.com"), OffsetDateTime.now());

        GithubLink savedGithubLink = Objects.requireNonNull(githubLinkRepository.save(githubLink));

        assertEquals(githubLink.getId(), savedGithubLink.getId());
        assertEquals(githubLink.getDefaultBranch(), savedGithubLink.getDefaultBranch());
        assertEquals(githubLink.getForksCount(), savedGithubLink.getForksCount());
    }

    @Test
    public void testUpdateGithubLink() {
        GithubLink githubLink =
            createGithubLink(3124L, "master", 2L, URI.create("https://test.com"), OffsetDateTime.now());

        GithubLink savedGithubLink = Objects.requireNonNull(githubLinkRepository.save(githubLink));
        savedGithubLink.setForksCount(2L);

        githubLinkRepository.save(savedGithubLink);

        assertEquals(githubLink.getId(), savedGithubLink.getId());
        assertEquals(githubLink.getDefaultBranch(), savedGithubLink.getDefaultBranch());
        assertNotEquals(githubLink.getForksCount(), savedGithubLink.getForksCount());
    }
}
