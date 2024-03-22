package edu.java.bot.service;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.WebHandlerBot;
import lombok.RequiredArgsConstructor;
import edu.java.bot.entity.LinkUpdateRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class LinkUpdateService {
    private static final Logger LOGGER = LogManager.getLogger();
    private final WebHandlerBot bot;

    public void sendUpdateNotification(LinkUpdateRequest request) {
        request.tgChatIds()
            .forEach(tgChatId -> {
                LOGGER.info("Send an update to chat with id %d".formatted(tgChatId));

                bot.execute(new SendMessage(
                    tgChatId,
                    "По [ссылке](%s) произошло обновление:\n%s".formatted(
                        request.url().toString(),
                        request.description()
                    )
                ).parseMode(ParseMode.Markdown));
            });
    }
}
