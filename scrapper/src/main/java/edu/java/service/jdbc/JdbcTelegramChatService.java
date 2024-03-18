package edu.java.service.jdbc;

import edu.java.domain.jdbc.JdbcTelegramChatRepository;
import edu.java.entity.TelegramChat;
import edu.java.entity.dto.ChatOperationResponse;
import edu.java.exception.TelegramChatAlreadyRegistered;
import edu.java.exception.TelegramChatNotExistsException;
import edu.java.service.TelegramChatService;
import java.time.OffsetDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JdbcTelegramChatService implements TelegramChatService {
    private final JdbcTelegramChatRepository telegramChatRepository;

    @Override
    public ChatOperationResponse register(Long tgChatId) {
        try {
            TelegramChat chat = new TelegramChat();
            chat.setId(tgChatId);
            chat.setRegisteredAt(OffsetDateTime.now());

            TelegramChat savedEntity = telegramChatRepository.add(chat);

            return new ChatOperationResponse(savedEntity != null);
        } catch (DuplicateKeyException ignored) {
            throw new TelegramChatAlreadyRegistered(tgChatId);
        }
    }

    @Override
    public ChatOperationResponse unregister(Long tgChatId) {
        try {
            TelegramChat chat = new TelegramChat();
            chat.setId(tgChatId);
            chat.setRegisteredAt(OffsetDateTime.now());

            TelegramChat deletedEntity = telegramChatRepository.remove(chat);

            return new ChatOperationResponse(deletedEntity != null);
        } catch (EmptyResultDataAccessException ignored) {
            throw new TelegramChatNotExistsException(tgChatId);
        }
    }
}
