package edu.java.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TelegramChatAlreadyRegistered extends ResponseStatusException {
    public TelegramChatAlreadyRegistered(Long tgChatId) {
        super(HttpStatus.CONFLICT, "Telegram chat with id %d is already registered".formatted(tgChatId));
    }
}
