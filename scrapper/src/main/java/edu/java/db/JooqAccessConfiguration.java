package edu.java.db;

import edu.java.repository.jooq.JooqGithubLinkRepository;
import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.repository.jooq.JooqStackOverflowLinkRepository;
import edu.java.repository.jooq.JooqTelegramChatRepository;
import edu.java.service.GithubLinkService;
import edu.java.service.LinkService;
import edu.java.service.StackOverflowLinkService;
import edu.java.service.TelegramChatService;
import edu.java.service.jooq.JooqGithubLinkService;
import edu.java.service.jooq.JooqLinkService;
import edu.java.service.jooq.JooqStackOverflowLinkService;
import edu.java.service.jooq.JooqTelegramChatService;
import edu.java.util.LinkUtil;
import org.jooq.DSLContext;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@SuppressWarnings("unused")
@Configuration
@ConditionalOnProperty(prefix = "scrapper", name = "database-access-type", havingValue = "jooq")
public class JooqAccessConfiguration {
    @Bean
    public JooqLinkRepository jooqLinkRepository(DSLContext dslContext) {
        return new JooqLinkRepository(dslContext);
    }

    @Bean
    public LinkService linkService(JooqLinkRepository jooqLinkRepository, LinkUtil linkUtil) {
        return new JooqLinkService(jooqLinkRepository, linkUtil);
    }

    @Bean
    public JooqTelegramChatRepository jooqTelegramChatRepository(DSLContext dslContext) {
        return new JooqTelegramChatRepository(dslContext);
    }

    @Bean
    public TelegramChatService telegramChatService(JooqTelegramChatRepository jooqTelegramChatRepository) {
        return new JooqTelegramChatService(jooqTelegramChatRepository);
    }

    @Bean
    public JooqGithubLinkRepository jooqGitHubLinkRepository(DSLContext dslContext) {
        return new JooqGithubLinkRepository(dslContext);
    }

    @Bean
    public GithubLinkService gitHubLinkService(JooqGithubLinkRepository jooqGitHubLinkRepository) {
        return new JooqGithubLinkService(jooqGitHubLinkRepository);
    }

    @Bean
    public JooqStackOverflowLinkRepository jooqStackOverflowLinkRepository(DSLContext dslContext) {
        return new JooqStackOverflowLinkRepository(dslContext);
    }

    @Bean
    public StackOverflowLinkService stackOverflowLinkService(
        JooqStackOverflowLinkRepository jooqStackOverflowLinkRepository
    ) {
        return new JooqStackOverflowLinkService(jooqStackOverflowLinkRepository);
    }
}
