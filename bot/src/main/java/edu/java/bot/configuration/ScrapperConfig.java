package edu.java.bot.configuration;


import edu.java.bot.service.ScrapperClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ScrapperConfig {
    private final ApplicationConfig config;
    //TODO Я еще не разобрался как подтягивать с другого модуля конфиги этаго
    @Bean
    public ScrapperClient scrapperClient() {
        return new ScrapperClient("http://localhost:8080");
    }
}
