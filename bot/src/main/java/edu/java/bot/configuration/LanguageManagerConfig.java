package edu.java.bot.configuration;

import edu.java.bot.utils.LanguageManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LanguageManagerConfig {
    private final ApplicationConfig applicationConfig;
    @Bean
    public LanguageManager languageManager() {
        return new LanguageManager("lang.json");
    }
}
