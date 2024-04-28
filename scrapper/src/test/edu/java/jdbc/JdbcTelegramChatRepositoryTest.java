package edu.java.jdbc;

import edu.java.IntegrationTest;
import edu.java.entity.TelegramChat;
import edu.java.repository.jdbc.JdbcTelegramChatRepository;
import java.time.OffsetDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class JdbcTelegramChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcTelegramChatRepository jdbcTelegramChatRepository;

    @DynamicPropertySource
    public static void setJdbcAccessType(DynamicPropertyRegistry registry) {
        registry.add("scrapper.database-access-type", () -> "jdbc");
    }

    @Test
    @Transactional
    @Rollback
    public void testAddChat() {
        TelegramChat telegramChat = new TelegramChat();
        telegramChat.setId(123L);
        telegramChat.setRegisteredAt(OffsetDateTime.now());

        assertTrue(jdbcTelegramChatRepository.findAll().isEmpty());
        jdbcTelegramChatRepository.add(telegramChat);
        assertFalse(jdbcTelegramChatRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testAddDuplicateChatThrowsException() {
        TelegramChat telegramChat = new TelegramChat();
        telegramChat.setId(1L);
        telegramChat.setRegisteredAt(OffsetDateTime.now());

        jdbcTelegramChatRepository.add(telegramChat);
        assertThrows(DuplicateKeyException.class, () -> jdbcTelegramChatRepository.add(telegramChat));
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveChat() {
        TelegramChat telegramChat = new TelegramChat();
        telegramChat.setId(123L);
        telegramChat.setRegisteredAt(OffsetDateTime.now());

        assertTrue(jdbcTelegramChatRepository.findAll().isEmpty());
        jdbcTelegramChatRepository.add(telegramChat);
        jdbcTelegramChatRepository.remove(telegramChat);
        assertTrue(jdbcTelegramChatRepository.findAll().isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    public void testRemoveNonExistingChatThrowsException() {
        TelegramChat telegramChat = new TelegramChat();
        telegramChat.setId(123L);
        telegramChat.setRegisteredAt(OffsetDateTime.now());

        assertTrue(jdbcTelegramChatRepository.findAll().isEmpty());
        assertThrows(EmptyResultDataAccessException.class, () -> jdbcTelegramChatRepository.remove(telegramChat));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindAllChats() {
        TelegramChat telegramChat1 =  new TelegramChat();
        telegramChat1.setId(1L);
        telegramChat1.setRegisteredAt(OffsetDateTime.now());

        TelegramChat telegramChat2 = new TelegramChat();
        telegramChat2.setId(2L);
        telegramChat2.setRegisteredAt(OffsetDateTime.now());

        List<TelegramChat> chats = List.of(
            telegramChat1, telegramChat2
        );

        chats.forEach(jdbcTelegramChatRepository::add);
        assertEquals(chats.size(), jdbcTelegramChatRepository.findAll().size());
    }
}
