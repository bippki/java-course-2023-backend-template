package edu.java.repository;

import edu.java.entity.TelegramChat;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class TelegramChatRepository implements ITelegramChatRepository {
    private final Map<Long, TelegramChat> repository = new HashMap<>();

    @Override
    public Optional<TelegramChat> findById(Long id) {
        return repository.containsKey(id)
            ? Optional.of(repository.get(id))
            : Optional.empty();
    }

    @Override
    public TelegramChat save(TelegramChat telegramChat) {
        repository.put(telegramChat.getId(), telegramChat);

        return telegramChat;
    }

    @Override
    public void delete(TelegramChat telegramChat) {
        repository.remove(telegramChat.getId());
    }
}
