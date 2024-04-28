package edu.java.jpa;

import edu.java.IntegrationTest;
import edu.java.entity.Link;
import edu.java.entity.TelegramChat;
import edu.java.repository.jpa.JpaLinkRepository;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest
public class JpaLinkRepositoryTest extends IntegrationTest {
    @Autowired
    private JpaLinkRepository linkRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DynamicPropertySource
    public static void setJpaAccessType(DynamicPropertyRegistry registry) {
        registry.add("scrapper.database-access-type", () -> "jpa");
    }

    @Test
    @Transactional
    @Rollback
    public void testAddLink() {
        final Link link = new Link()
            .setUrl(URI.create("https://github.com"))
            .setLastUpdatedAt(OffsetDateTime.now());

        assertTrue(linkRepository.findAll().isEmpty());
        assertEquals(link.getUrl(), linkRepository.save(link).getUrl());
        assertFalse(linkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveLink() {
        assertTrue(linkRepository.findAll().isEmpty());

        final Link link = linkRepository.save(new Link()
            .setUrl(URI.create("https://github.com"))
            .setLastUpdatedAt(OffsetDateTime.now()));

        linkRepository.delete(link);
        assertTrue(linkRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllLinks() {
        final List<Link> links = List.of(
            new Link().setUrl(URI.create("https://test1.com")).setLastUpdatedAt(OffsetDateTime.now()),
            new Link().setUrl(URI.create("https://test2.com")).setLastUpdatedAt(OffsetDateTime.now())
        );

        linkRepository.saveAll(links);
        assertEquals(links.size(), linkRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddChatToLinkAndFindAllForChat() {
        final Long chatId = 10L;

        jdbcTemplate.update("INSERT INTO telegram_chat (id, registered_at) VALUES (?, NOW())", chatId);
        final Link link = linkRepository.save(new Link()
                .setUrl(URI.create("https://test1.com"))
                .setLastUpdatedAt(OffsetDateTime.now()))
            .setTelegramChats(Set.of(new TelegramChat().setId(chatId)));

        assertEquals(List.of(link), linkRepository.findAllForChat(chatId));
    }
}
