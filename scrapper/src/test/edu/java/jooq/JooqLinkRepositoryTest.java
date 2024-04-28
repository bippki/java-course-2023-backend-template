package edu.java.jooq;

import edu.java.IntegrationTest;
import edu.java.entity.Link;
import edu.java.repository.jooq.JooqLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class JooqLinkRepositoryTest extends IntegrationTest {

    @Autowired
    private JooqLinkRepository linkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    public static void setJooqAccessType(DynamicPropertyRegistry registry) {
        registry.add("scrapper.database-access-type", () -> "jooq");
    }

    @Test
    @Transactional
    @Rollback
    public void testLinkAdd() {
        Link link = createTestLink("https://github.com");
        assertTrue(linkRepository.findAll().isEmpty());

        Link addedLink = linkRepository.add(link);
        assertEquals(link.getUrl(), addedLink.getUrl());
        assertFalse(linkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddDuplicateKeyException() {
        Link link = createTestLink("https://github.com");
        linkRepository.add(link);

        assertThrows(DuplicateKeyException.class, () -> linkRepository.add(link));
    }

    @Test
    @Transactional
    @Rollback
    public void testLinkRemove() {
        assertTrue(linkRepository.findAll().isEmpty());

        Link link = linkRepository.add(createTestLink("https://github.com"));
        assertEquals(link.getUrl(), linkRepository.remove(link).getUrl());
        assertTrue(linkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testLinkFindAll() {
        List<Link> links = List.of(
            createTestLink("https://test1.com"),
            createTestLink("https://test2.com")
        );
        links.forEach(linkRepository::add);

        assertEquals(links.size(), linkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    public void testLinkFindAllForChat() {
        Long chatId = 10L;
        Link link = linkRepository.add(createTestLink("https://test1.com"));
        jdbcTemplate.update("INSERT INTO telegram_chat (id, registered_at) VALUES (?, NOW())", chatId);
        linkRepository.connectChatToLink(chatId, link.getId());

        assertEquals(List.of(link), linkRepository.findAllForChat(chatId));
    }

    @Test
    @Transactional
    @Rollback
    public void testConnectNonExistingChatToLinkThrowsException() {
        Link savedLink = linkRepository.add(createTestLink("https://test1.com"));

        assertThrows(DataIntegrityViolationException.class,
            () -> linkRepository.connectChatToLink(10L, savedLink.getId()));
    }

    private Link createTestLink(String url) {
        return new Link()
            .setUrl(URI.create(url))
            .setLastUpdatedAt(OffsetDateTime.now());
    }
}
