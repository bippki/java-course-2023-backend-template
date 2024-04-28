package edu.java.service.jpa;

import edu.java.entity.TelegramChat;
import edu.java.entity.dto.ChatOperationResponse;
import edu.java.exception.TelegramChatAlreadyRegistered;
import edu.java.exception.TelegramChatNotExistsException;
import edu.java.repository.jpa.JpaTelegramChatRepository;
import edu.java.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import java.time.OffsetDateTime;

@RequiredArgsConstructor
public class JpaTelegramChatService implements TelegramChatService {
    private final JpaTelegramChatRepository jpaTelegramChatRepository;

    @Override
    public ChatOperationResponse register(Long tgChatId) {
        jpaTelegramChatRepository.findById(tgChatId)
            .ifPresent(s -> {
                throw new TelegramChatAlreadyRegistered(s.getId());
            });

        jpaTelegramChatRepository.save(new TelegramChat()
            .setId(tgChatId)
            .setRegisteredAt(OffsetDateTime.now()));

        return new ChatOperationResponse(true);
    }

    @Override
    public ChatOperationResponse unregister(Long tgChatId) {
        jpaTelegramChatRepository.delete(jpaTelegramChatRepository.findById(tgChatId)
            .orElseThrow(() -> new TelegramChatNotExistsException(tgChatId)));

        return new ChatOperationResponse(true);
    }
}
