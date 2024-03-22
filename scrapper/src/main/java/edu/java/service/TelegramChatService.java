package edu.java.service;

import edu.java.entity.TelegramChat;
import edu.java.exception.TelegramChatAlreadyRegistered;
import edu.java.exception.TelegramChatNotExistsException;
import edu.java.repository.TelegramChatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TelegramChatService {
    private final TelegramChatRepository chatRepository;

    public void registerChat(@PathVariable Long id) {
        chatRepository.findById(id).ifPresent(ignore -> {
            throw new TelegramChatAlreadyRegistered(id);
        });

        chatRepository.save(new TelegramChat(id, new ArrayList<>()));
    }

    public void deleteChat(@PathVariable Long id) {
        chatRepository.delete(chatRepository.findById(id)
            .orElseThrow(() -> new TelegramChatNotExistsException(id)));
    }

    public Optional<TelegramChat> getChat(@PathVariable Long id) {
        return chatRepository.findById(id);
    }
}
