package edu.java.service;

import edu.java.entity.dto.ChatOperationResponse;


public interface TelegramChatService {
    ChatOperationResponse register(Long tgChatId);
    ChatOperationResponse unregister(Long tgChatId);
}
