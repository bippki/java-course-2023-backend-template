package edu.java.service.jooq;

import edu.java.entity.TelegramChat;
import edu.java.entity.dto.ChatOperationResponse;
import edu.java.exception.TelegramChatAlreadyRegistered;
import edu.java.exception.TelegramChatNotExistsException;
import edu.java.repository.jooq.JooqTelegramChatRepository;
import edu.java.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class JooqTelegramChatService implements TelegramChatService {
    private final JooqTelegramChatRepository jooqTelegramChatRepository;
    @Override
    public ChatOperationResponse register(Long tgChatId) {
        try {
            TelegramChat savedEntity = jooqTelegramChatRepository.add(new TelegramChat()
                .setId(tgChatId)
                .setRegisteredAt(OffsetDateTime.now()));
            return new ChatOperationResponse(savedEntity != null);
        } catch (DuplicateKeyException ignored) {
            throw new TelegramChatAlreadyRegistered(tgChatId);
        }
    }
    @Override
    public ChatOperationResponse unregister(Long tgChatId) {
        try {
            TelegramChat deletedEntity = jooqTelegramChatRepository.remove(new TelegramChat()
                .setId(tgChatId));
            if (deletedEntity == null) {
                throw new TelegramChatNotExistsException(tgChatId);
            }
            return new ChatOperationResponse(true);
        } catch (EmptyResultDataAccessException ignored) {
            throw new TelegramChatNotExistsException(tgChatId);
        }
    }
}
