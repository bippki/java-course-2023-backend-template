package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exception.ApiErrorResponseException;
import edu.java.bot.service.Scrapper;
import edu.java.bot.utils.LanguageManager;
import edu.java.bot.utils.LinkUtil;
import entity.dto.LinkResponse;
import entity.dto.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import entity.dto.AddLinkRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TrackCommand implements Command {
    private static final String COMMAND_NAME = "/track";
    private final Scrapper client;
    private final LanguageManager languageManager;

    @Override
    public String command() {
        return COMMAND_NAME;
    }

    @Override
    public String description() {
        return languageManager.translate("command.track.description");
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String[] parameters = update.message().text().split(" ");

        if (parameters.length != 2) {
            return new SendMessage(chatId, languageManager.translate("command.track.error.invalid_syntax"));
        }

        URI link = LinkUtil.parse(parameters[1]);

        if (link == null) {
            return new SendMessage(chatId, languageManager.translate("command.track.error.invalid_link"));
        }

        // Проверяем, есть ли уже такая ссылка в списке
        if (linkAlreadyExists(chatId, link)) {
            return new SendMessage(chatId, languageManager.translate("command.track.error.link_already_exists"));
        }

        return new SendMessage(chatId, getResponseMessage(chatId, link))
            .disableWebPagePreview(true)
            .parseMode(ParseMode.Markdown);
    }

    private boolean linkAlreadyExists(Long chatId, URI link) {
        ResponseEntity<ListLinksResponse> responseEntity = client.getAllLinksForChat(chatId).block();

        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            List<LinkResponse> links = responseEntity.getBody().links();

            for (LinkResponse existingLink : links) {
                if (existingLink.url().equals(link)) {
                    return true;
                }
            }
        }

        return false;
    }

    private String getResponseMessage(Long chatId, URI link) {
        return client.addLink(chatId, new AddLinkRequest(link))
            .map(response -> {
                if (HttpStatus.OK.equals(response.getStatusCode())
                    && response.getBody() != null) {
                    return languageManager.translate("command.track.response.link_added_success")
                        .formatted(response.getBody().url());
                }
                return languageManager.translate("command.error.default");
            })
            .onErrorResume(
                ApiErrorResponseException.class,
                error -> Mono.just(error.getApiErrorResponse().description())
            )
            .block();
    }
}