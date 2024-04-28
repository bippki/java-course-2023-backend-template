package edu.java.repository.jpa;

import edu.java.entity.TelegramChat;
import org.springframework.data.repository.CrudRepository;

public interface JpaTelegramChatRepository extends CrudRepository<TelegramChat, Long> {
}
