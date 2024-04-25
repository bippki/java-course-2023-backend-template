package edu.java.jdbc;

import edu.java.IntegrationTest;
import edu.java.entity.Link;
import edu.java.repository.jdbc.JdbcLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
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
class JdbcLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcLinkRepository jdbcLinkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("scrapper.database-access-type", () -> "jdbc");
    }

    @Test
    @Transactional
    @Rollback
    public void testAddLink() {
        Link link = new Link();
        link.setUrl(URI.create("https://github.com"));
        link.setLastUpdatedAt(OffsetDateTime.now());

        assertTrue(jdbcLinkRepository.findAll().isEmpty());
        assertEquals(link.getUrl(), jdbcLinkRepository.add(link).getUrl());
        assertFalse(jdbcLinkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddDuplicateLinkThrowsException() {
        Link link = new Link();
        link.setUrl(URI.create("https://github.com"));
        link.setLastUpdatedAt(OffsetDateTime.now());

        jdbcLinkRepository.add(link);
        assertThrows(DuplicateKeyException.class, () -> jdbcLinkRepository.add(link));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLink() {
        assertTrue(jdbcLinkRepository.findAll().isEmpty());

        Link link = new Link();
        link.setUrl(URI.create("https://github.com"));
        link.setLastUpdatedAt(OffsetDateTime.now());

        Link query = jdbcLinkRepository.add(link);
        assertEquals(query.getUrl(), jdbcLinkRepository.remove(query).getUrl());
        assertTrue(jdbcLinkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveNonExistingLinkThrowsException() {
        Link link = new Link();
        link.setId(1L);
        link.setUrl(URI.create("https://github.com"));
        link.setLastUpdatedAt(OffsetDateTime.now());

        assertTrue(jdbcLinkRepository.findAll().isEmpty());
        assertThrows(EmptyResultDataAccessException.class, () -> jdbcLinkRepository.remove(link));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllLinks() {
        Link link1 = new Link();
        link1.setUrl(URI.create("https://ia601708.us.archive.org/24/items/KINGASSRIPPER/KINGASSRIPPER.mp4"));
        link1.setLastUpdatedAt(OffsetDateTime.now());

        Link link2 = new Link();
        link2.setUrl(URI.create("https://www.youtube.com/watch?v=rIrNIzy6U_g"));
        link2.setLastUpdatedAt(OffsetDateTime.now());

        List<Link> links = List.of(
            link1, link2
        );

        links.forEach(jdbcLinkRepository::add);
        assertEquals(links.size(), jdbcLinkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    public void testConnectChatToLinkAndFindAllForChat() {
        final Long chatId = 10L;

        Link link = new Link();
        link.setUrl(URI.create("https://ia601708.us.archive.org/24/items/KINGASSRIPPER/KINGASSRIPPER.mp4"));
        link.setLastUpdatedAt(OffsetDateTime.now());

        Link query = jdbcLinkRepository.add(link);

        jdbcTemplate.update("INSERT INTO telegram_chat (id, registered_at) VALUES (?, NOW())", chatId);
        jdbcLinkRepository.connectChatToLink(chatId, query.getId());
        assertEquals(List.of(query), jdbcLinkRepository.findAllForChat(chatId));
    }

}
