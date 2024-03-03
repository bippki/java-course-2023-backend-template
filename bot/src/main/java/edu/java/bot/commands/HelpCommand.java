package edu.java.bot.commands;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.utils.LanguageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@RequiredArgsConstructor
public class HelpCommand implements Command {
    private static final String COMMAND_NAME = "/help";
    private final List<Command> commands;
    private final LanguageManager languageManager;

    @Override
    public String command() {
        return COMMAND_NAME;
    }

    @Override
    public String description() {
        return languageManager.translate("command.help.description");
    }

    @Override
    public SendMessage handle(Update update) {
        return new SendMessage(update.message().chat().id(), buildResponse())
            .parseMode(ParseMode.HTML);
    }

    private String buildResponse() {
        StringBuilder responseMessage =
            new StringBuilder().append(languageManager.translate("command.help.response.available_commands"))
                .append(":\n");

        commands.forEach(command -> responseMessage.append("<b>%s</b> - %s\n".formatted(
            command.command(),
            command.description()
        )));

        return responseMessage.toString();
    }
}
