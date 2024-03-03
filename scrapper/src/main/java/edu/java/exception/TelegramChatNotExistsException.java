package edu.java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TelegramChatNotExistsException extends ResponseStatusException {
    public TelegramChatNotExistsException(Long tgChatId) {
        super(HttpStatus.NOT_FOUND, "Telegram chat with id %d is not exists".formatted(tgChatId));
    }
}
