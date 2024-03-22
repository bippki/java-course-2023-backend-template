package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exception.ApiErrorResponseException;
import edu.java.bot.service.Scrapper;
import edu.java.bot.utils.LanguageManager;
import lombok.RequiredArgsConstructor;
import edu.java.bot.entity.LinkResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
@Component
@RequiredArgsConstructor
public class ListCommand implements Command {
    private static final String COMMAND_NAME = "/list";
    private final Scrapper client;
    private final LanguageManager languageManager;


    @Override
    public String command() {
        return COMMAND_NAME;
    }

    @Override
    public String description() {
        return languageManager.translate("command.list.description");
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();

        return new SendMessage(chatId, getResponseMessage(chatId))
            .disableWebPagePreview(true)
            .parseMode(ParseMode.Markdown);
    }

    private String getResponseMessage(Long chatId) {
        return client.getAllLinksForChat(chatId)
            .map(response -> {
                if (HttpStatus.OK.equals(response.getStatusCode())
                    && response.getBody() != null && response.getBody().links() != null) {
                    if (response.getBody().links().isEmpty()) {
                        return languageManager.translate("command.list.error.no_links");
                    }
                    return buildResponse(response.getBody().links());
                }
                return languageManager.translate("command.error.default");
            })
            .onErrorResume(
                ApiErrorResponseException.class,
                error -> Mono.just((error.getApiErrorResponse().description()))
            )
            .block();
    }

    private String buildResponse(List<LinkResponse> linkResponses) {
        StringBuilder responseMessage = new StringBuilder()
            .append(languageManager.translate("command.list.response.tracked_links")).append(":\n");
        List<String> urls = linkResponses.stream()
            .map(linkResponse -> linkResponse.url().toString())
            .toList();

        for (int i = 0; i < urls.size(); i++) {
            responseMessage.append(i + 1).append(": ").append(urls.get(i)).append("\n");
        }

        return responseMessage.toString();
    }
}
