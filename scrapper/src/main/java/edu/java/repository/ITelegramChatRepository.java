package edu.java.repository;

import edu.java.entity.TelegramChat;
import java.util.Optional;

public interface ITelegramChatRepository {
    Optional<TelegramChat> findById(Long id);

    TelegramChat save(TelegramChat telegramChat);

    void delete(TelegramChat telegramChat);
}
