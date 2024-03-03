package edu.java.bot.configuration;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "bot", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotEmpty
     String telegramToken,
     String languageFilePath,
    ApiLink apiLink) {
    public record ApiLink(String scrapper) { }
}
