package edu.java.configuration;

import edu.java.client.BotClient;
import edu.java.entity.dto.bot.LinkUpdateRequest;
import edu.java.service.LinkService;
import edu.java.util.client.BaseClientProcessor;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {

    private final List<BaseClientProcessor> clientProcessors;
    private final LinkService linkService;
    private final BotClient botClient;
    private final ApplicationConfig config;

    @Scheduled(fixedDelayString = "${scrapper.scheduler.interval}")
    public void update() {
        linkService.listAllWithInterval(config.scheduler().linkLastCheckInterval()).forEach(link -> {
            for (BaseClientProcessor clientProcessor : clientProcessors) {
                if (clientProcessor.isCandidate(link.getUrl())) {
                    clientProcessor.getUpdate(link)
                        .filter(Objects::nonNull)
                        .map(update -> new LinkUpdateRequest(
                            link.getId(),
                            link.getUrl(),
                            update,
                            linkService.getAllChatsForLink(link.getId())
                        ))
                        .subscribe(botClient::sendUpdate);
                    link.setLastUpdatedAt(OffsetDateTime.now());
                    linkService.updateLink(link);
                    break;
                }
            }
        });
    }
}
