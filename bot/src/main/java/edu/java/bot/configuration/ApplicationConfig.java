package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {

    @Value("${app.telegram-token}")
    @Getter
    private String telegramToken;

    @Value("${app.telegram-bot-name}")
    @Getter
    private String botName;

    @Value("${app.telegram-creator-id}")
    @Getter
    private Integer creatorId;

    @Value("${server.port}")
    @Getter
    private int serverPort;
}
