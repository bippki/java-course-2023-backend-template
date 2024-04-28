package edu.java.jooq;


import edu.java.IntegrationTest;
import edu.java.entity.TelegramChat;
import edu.java.repository.jooq.JooqTelegramChatRepository;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
@SpringBootTest
public class JooqTelegramChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JooqTelegramChatRepository telegramChatRepository;

    @DynamicPropertySource
    public static void setJooqAccessType(DynamicPropertyRegistry registry) {
        registry.add("scrapper.database-access-type", () -> "jooq");
    }

    @Test
    @Transactional
    @Rollback
    public void testAddTelegramChat() {
        final TelegramChat telegramChat = new TelegramChat()
            .setId(123L)
            .setRegisteredAt(OffsetDateTime.now());
        assertTrue(telegramChatRepository.findAll().isEmpty());
        telegramChatRepository.add(telegramChat);
        assertFalse(telegramChatRepository.findAll().isEmpty());
    }
    @Test
    @Transactional
    @Rollback
    public void testAddDuplicateKeyException() {
        final TelegramChat telegramChat = new TelegramChat()
            .setId(1L)
            .setRegisteredAt(OffsetDateTime.now());
        telegramChatRepository.add(telegramChat);
        assertThrows(DuplicateKeyException.class, () -> telegramChatRepository.add(telegramChat));
    }
    @Test
    @Transactional
    @Rollback
    public void testRemoveTelegramChat() {
        final TelegramChat telegramChat = new TelegramChat()
            .setId(123L)
            .setRegisteredAt(OffsetDateTime.now());
        assertTrue(telegramChatRepository.findAll().isEmpty());
        telegramChatRepository.add(telegramChat);
        telegramChatRepository.remove(telegramChat);
        assertTrue(telegramChatRepository.findAll().isEmpty());
    }
    @Test
    @Transactional
    @Rollback
    public void testFindAllTelegramChat() {
        final List<TelegramChat> chats = List.of(
            new TelegramChat().setId(1L).setRegisteredAt(OffsetDateTime.now()),
            new TelegramChat().setId(2L).setRegisteredAt(OffsetDateTime.now())
        );
        chats.forEach(telegramChatRepository::add);
        assertEquals(chats.size(), telegramChatRepository.findAll().size());
    }
}
