package edu.java.jooq;

import edu.java.IntegrationTest;
import edu.java.entity.GithubLink;
import edu.java.entity.Link;
import edu.java.repository.jooq.JooqGithubLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JooqGitHubLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JooqGithubLinkRepository gitHubLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    public static void setJooqAccessType(DynamicPropertyRegistry registry) {
        registry.add("scrapper.database-access-type", () -> "jooq");
    }

    @Test
    @Transactional
    @Rollback
    public void testNonExistsGithubLinkThrowsException() {
        Link nonExistsLink = createTestLink(Long.MAX_VALUE, "https://url.com");
        assertNull(gitHubLinkRepository.getLink(nonExistsLink));
    }

    @Test
    @Transactional
    @Rollback
    public void testAddExistsLink() {
        Link existsLink = insertLink("https://test.com");
        GithubLink gitHubLink = createGithubLink(existsLink.getId(), "master", 1L);

        GithubLink savedGitHubLink = gitHubLinkRepository.addLink(gitHubLink);

        assertEquals(gitHubLink.getId(), savedGitHubLink.getId());
        assertEquals(gitHubLink.getDefaultBranch(), savedGitHubLink.getDefaultBranch());
        assertEquals(gitHubLink.getForksCount(), savedGitHubLink.getForksCount());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddGitHubLinkReferenceNotExistsThrowsException() {
        GithubLink gitHubLink = createGithubLink(Long.MAX_VALUE, "master", 1L);

        assertThrows(DataIntegrityViolationException.class, () -> gitHubLinkRepository.addLink(gitHubLink));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateGithubLink() {
        Link existsLink = insertLink("https://test.com");
        GithubLink gitHubLink = createGithubLink(existsLink.getId(), "master", 2L);

        GithubLink savedGitHubLink = gitHubLinkRepository.addLink(gitHubLink);
        savedGitHubLink.setForksCount(3L);
        gitHubLinkRepository.updateLink(savedGitHubLink);

        assertEquals(gitHubLink.getId(), savedGitHubLink.getId());
        assertEquals(gitHubLink.getDefaultBranch(), savedGitHubLink.getDefaultBranch());
        assertNotEquals(gitHubLink.getForksCount(), savedGitHubLink.getForksCount());
    }

    private Link createTestLink(long id, String url) {
        return new Link()
            .setId(id)
            .setUrl(URI.create(url))
            .setLastUpdatedAt(OffsetDateTime.now());
    }

    private Link insertLink(String url) {
        Link link = createTestLink(0, url);
        return jdbcTemplate.queryForObject(
            "INSERT INTO link (url, last_updated_at) VALUES (?, ?) RETURNING *",
            new BeanPropertyRowMapper<>(Link.class),
            link.getUrl().toString(),
            link.getLastUpdatedAt()
        );
    }

    private GithubLink createGithubLink(long id, String defaultBranch, long forksCount) {
        GithubLink githubLink = new GithubLink();
        githubLink.setId(id);
        githubLink.setDefaultBranch(defaultBranch);
        githubLink.setForksCount(forksCount);
        return githubLink;
    }
}
