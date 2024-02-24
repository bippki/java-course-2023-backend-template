package edu.java.bot;

import edu.java.bot.configuration.ApplicationConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class BotApplication {
    @Autowired
    private ApplicationConfig config;
    public static void main(String[] args)  {
        SpringApplication.run(BotApplication.class, args);
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            botsApi.registerBot(new WebHandlerBot(
                config.getTelegramToken(),
                config.getBotName(),
                config.getCreatorId()
            ));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
