package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.utils.LanguageManager;
import edu.java.bot.utils.LinkUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class LanguageCommand implements Command {
    private static final String COMMAND_NAME = "/language";

    private final LanguageManager languageManager;

    @Override
    public String command() {
        return COMMAND_NAME;
    }

    @Override
    public String description() {
        return languageManager.translate("command.language.response.description");
    }

    @Override
    public SendMessage handle(Update update) {
        Long chatId = update.message().chat().id();
        String[] parameters = LinkUtil.prepare(update.message().text());

        if (parameters.length != 2) {
            KeyboardButton buttonRu = new KeyboardButton("/language ru");
            KeyboardButton buttonEn = new KeyboardButton("/language en");

            ReplyKeyboardMarkup replyMarkup = new ReplyKeyboardMarkup(
                buttonRu, buttonEn).oneTimeKeyboard(true);

            return new SendMessage(chatId, "Выберите язык:")
                .replyMarkup(replyMarkup);
        }

        if (parameters[1].equals("ru")) {
            languageManager.setCurrentLanguage("ru");
        } else if (parameters[1].equals("en")) {
            languageManager.setCurrentLanguage("en");
        }
        return new SendMessage(chatId, languageManager.translate("command.language.set")).parseMode(ParseMode.HTML);
    }

}
