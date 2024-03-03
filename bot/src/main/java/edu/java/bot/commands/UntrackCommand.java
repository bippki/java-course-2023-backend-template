package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import java.net.URI;
import java.util.List;
import edu.java.bot.exception.ApiErrorResponseException;
import edu.java.bot.service.Scrapper;
import edu.java.bot.utils.LanguageManager;
import edu.java.bot.utils.LinkUtil;
import entity.dto.LinkResponse;
import entity.dto.ListLinksResponse;
import lombok.RequiredArgsConstructor;
import entity.dto.RemoveLinkRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UntrackCommand implements Command {
    private static final String COMMAND_NAME = "/untrack";
    private final Scrapper client;
    private final LanguageManager languageManager;

    @Override
    public String command() {
        return COMMAND_NAME;
    }

    @Override
    public String description() {
        return languageManager.translate("command.untrack.description");
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String[] parameters = update.message().text().split(" ");

        if (parameters.length != 2) {
            return new SendMessage(chatId, languageManager.translate("command.untrack.error.invalid_syntax"));
        }


        URI link = LinkUtil.parse(parameters[1]);

        if (link == null) {
            if (!parameters[1].matches("\\d+")) {
                return new SendMessage(chatId, languageManager.translate("command.untrack.error.invalid_index"));
            }
            int index;

            try {
                 index = Math.min(Integer.parseInt(parameters[1]), Integer.MAX_VALUE);
            } catch (Exception e) {
                return new SendMessage(chatId, languageManager.translate("command.untrack.error.invalid_index"));
            }

            SendMessage message = new SendMessage(chatId, removeLinkByIndex(chatId, index - 1))
                .disableWebPagePreview(true)
                .parseMode(ParseMode.Markdown);
            return message;
        }
        return new SendMessage(chatId, removeLink(chatId, link))
            .disableWebPagePreview(true)
            .parseMode(ParseMode.Markdown);
    }

    private String removeLink(Long chatId, URI link) {
        return client.removeLink(chatId, new RemoveLinkRequest(link))
            .map(response -> {
                if (HttpStatus.OK.equals(response.getStatusCode())
                    && response.getBody() != null) {
                    return languageManager.translate("command.untrack.response.link_removed_success")
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

    private String removeLinkByIndex(Long chatId, int index) {
        // Получаем список ссылок для чата
        ResponseEntity<ListLinksResponse> responseEntity = client.getAllLinksForChat(chatId).block();

        if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
            List<LinkResponse> links = responseEntity.getBody().links();

            // Проверяем, что индекс в пределах списка ссылок
            if (index >= 0 && index < links.size()) {
                URI linkToRemove = links.get(index).url();

                // Выполняем запрос на удаление ссылки
                return client.removeLink(chatId, new RemoveLinkRequest(linkToRemove))
                    .map(response -> {
                        if (HttpStatus.OK.equals(response.getStatusCode()) && response.getBody() != null) {
                            return languageManager.translate("command.untrack.response.link_removed_success")
                                .formatted(response.getBody().url());
                        }
                        return languageManager.translate("command.error.default");
                    })
                    .onErrorResume(
                        ApiErrorResponseException.class,
                        error -> Mono.just(error.getApiErrorResponse().description())
                    )
                    .block();
            } else {
                return languageManager.translate("command.untrack.error.invalid_index");
            }
        } else {
            return languageManager.translate("command.error.default");
        }
    }
}
