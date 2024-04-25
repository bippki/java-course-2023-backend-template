package edu.java.jdbc;

import edu.java.IntegrationTest;
import edu.java.entity.GithubLink;
import edu.java.entity.Link;
import edu.java.repository.jdbc.JdbcGithubLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JdbcGithubLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JdbcGithubLinkRepository gitHubLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("scrapper.database-access-type", () -> "jdbc");
    }

    @Test
    @Transactional
    @Rollback
    public void testGetNonExistsGitHubLinkThrowsException() {
        Link nonExistsLink = createTestLink(Long.MAX_VALUE, "https://url.com");

        assertThrows(EmptyResultDataAccessException.class, () -> gitHubLinkRepository.getLink(nonExistsLink));
    }

    @Test
    @Transactional
    @Rollback
    public void testAddExistsLinkReturnedRightResult() {
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
    public void testAddGitHubLinkWhenReferenceLinkIsNotExistsThrowsException() {
        GithubLink gitHubLink = createGithubLink(Long.MAX_VALUE, "master", 1L);

        assertThrows(DataIntegrityViolationException.class, () -> gitHubLinkRepository.addLink(gitHubLink));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateGitHubLinkWorksRight() {
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
