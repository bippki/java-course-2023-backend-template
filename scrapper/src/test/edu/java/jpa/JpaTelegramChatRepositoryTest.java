package edu.java.jpa;

import edu.java.IntegrationTest;
import edu.java.entity.TelegramChat;
import edu.java.repository.jpa.JpaTelegramChatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class JpaTelegramChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JpaTelegramChatRepository telegramChatRepository;

    @DynamicPropertySource
    public static void setJpaAccessType(DynamicPropertyRegistry registry) {
        registry.add("scrapper.database-access-type", () -> "jpa");
    }

    private TelegramChat telegramChat1;
    private TelegramChat telegramChat2;

    @BeforeEach
    public void setup() {
        telegramChat1 = new TelegramChat().setId(1L).setRegisteredAt(OffsetDateTime.now());
        telegramChat2 = new TelegramChat().setId(2L).setRegisteredAt(OffsetDateTime.now());
        telegramChatRepository.saveAll(List.of(telegramChat1, telegramChat2));
    }

    @Test
    public void testAddTelegramChat() {
        final TelegramChat newTelegramChat = new TelegramChat().setId(3L).setRegisteredAt(OffsetDateTime.now());
        telegramChatRepository.save(newTelegramChat);
        assertEquals(List.of(telegramChat1, telegramChat2, newTelegramChat), telegramChatRepository.findAll());
    }

    @Test
    public void testRemoveTelegramChat() {
        telegramChatRepository.delete(telegramChat1);
        assertEquals(List.of(telegramChat2), telegramChatRepository.findAll());
    }

    @Test
    public void testFindAllTelegramChats() {
        assertEquals(List.of(telegramChat1, telegramChat2), telegramChatRepository.findAll());
    }
}
