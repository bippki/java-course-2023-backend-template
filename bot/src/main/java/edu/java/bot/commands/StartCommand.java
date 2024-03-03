package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.exception.ApiErrorResponseException;
import edu.java.bot.service.Scrapper;
import edu.java.bot.utils.LanguageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class StartCommand implements Command {
    private static final String COMMAND_NAME = "/start";
    private final Scrapper client;
    private final LanguageManager languageManager;


    @Override
    public String command() {
        return COMMAND_NAME;
    }

    @Override
    public String description() {
        return languageManager.translate("command.start.description");
    }

    @Override
    public SendMessage handle(Update update) {
        Chat chat = update.message().chat();

        return new SendMessage(chat.id(), getResponseMessage(chat))
            .parseMode(ParseMode.HTML);
    }

    private String getResponseMessage(Chat chat) {
        return languageManager.translate("command.start.response.hi") + ", <b>%s</b>!\n".formatted(chat.username())
            + client.registerChat(chat.id())
            .map(response -> {
                if (response.getStatusCode().equals(HttpStatus.OK)) {
                    return languageManager.translate("command.start.response.successfully_registered") + "\n";
                }
                return languageManager.translate("command.error.default") + "\n";
            })
            .onErrorResume(
                ApiErrorResponseException.class,
                error -> Mono.just(error.getApiErrorResponse().code() == 409 || error.getApiErrorResponse().code() == 0 ? languageManager.translate("command.start.error.conflict") + "\n" : " " )
            )
            .block()
            + languageManager.translate("command.start.response.view_commands");
    }
}
