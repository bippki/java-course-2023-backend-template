package edu.java.controller;

import edu.java.entity.TelegramChat;
import edu.java.service.TelegramChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
@RequestMapping("/tg-chat")
@RequiredArgsConstructor
public class TelegramChatController {
    private final TelegramChatService chatService;
    @GetMapping("/{id}")
    public Optional<TelegramChat> getChat(@RequestHeader Long tgChatId) {
        return chatService.getChat(tgChatId);
    }

    @PostMapping("/{id}")
    public void registerChat(@PathVariable Long id) {
        chatService.registerChat(id);
    }

    @DeleteMapping("/{id}")
    public void deleteChat(@PathVariable Long id) {
        chatService.deleteChat(id);
    }
}
