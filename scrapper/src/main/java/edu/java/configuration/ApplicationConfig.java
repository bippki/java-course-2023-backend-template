package edu.java.configuration;

import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import java.time.Duration;
@Validated
@ConfigurationProperties(prefix = "scrapper", ignoreUnknownFields = false)
public record ApplicationConfig(
    @NotNull
    Scheduler scheduler,
    ApiLink apiLink,
    AccessType databaseAccessType
) {
    public record Scheduler(
        boolean enable,
        @NotNull Duration interval,
        @NotNull Duration forceCheckDelay,
        @NotNull Duration linkLastCheckInterval) {
    }
    public record ApiLink(String gitHub, String stackOverflow, String bot) { }

    public enum AccessType {
        JDBC, JPA, JOOQ
    }
}
