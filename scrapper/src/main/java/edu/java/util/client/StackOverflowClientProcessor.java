package edu.java.util.client;

import edu.java.client.StackOverflowWebClient;
import edu.java.entity.Link;
import edu.java.entity.StackOverflowLink;
import edu.java.entity.dto.StackOverflowResponse;
import edu.java.service.StackOverflowLinkService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StackOverflowClientProcessor extends BaseClientProcessor {
    private static final Pattern STACK_OVERFLOW_PATH_PATTERN =
        Pattern.compile("^/questions/(?<questionId>\\d+)(/[\\w-]*)?(/)?$");
    private final StackOverflowWebClient stackOverflowClient;
    private final StackOverflowLinkService stackOverflowLinkService;

    public StackOverflowClientProcessor(
        StackOverflowWebClient stackOverflowClient,
        StackOverflowLinkService stackOverflowLinkService
    ) {
        super("stackoverflow.com");
        this.stackOverflowClient = stackOverflowClient;
        this.stackOverflowLinkService = stackOverflowLinkService;
    }

    @Override
    public boolean isCandidate(URI url) {
        return host.equals(url.getHost())
            && STACK_OVERFLOW_PATH_PATTERN.matcher(url.getPath()).matches();
    }

    @Override
    public Mono<String> getUpdate(Link link) {
        Matcher matcher = STACK_OVERFLOW_PATH_PATTERN.matcher(link.getUrl().getPath());
        if (!matcher.matches()) {
            return Mono.empty();
        }

        return stackOverflowClient.getQuestionsInfo(matcher.group("questionId"))
            .map(response -> processUpdate(response.items().getFirst(), link));
    }

    private String processUpdate(StackOverflowResponse.Question response, Link link) {
        Optional<StackOverflowLink> specificInfo = stackOverflowLinkService.getLink(link);
        StringBuilder update = new StringBuilder();

        if (response.lastActivityDate().isAfter(link.getLastUpdatedAt())) {
            update.append("Вопрос обновлён\n");
            update.append(updateField("Количество ответов", specificInfo.map(StackOverflowLink::getAnswerCount).orElse(null), response.answerCount()));
            update.append(updateField("Количество очков", specificInfo.map(StackOverflowLink::getScore).orElse(null), response.score()));

            StackOverflowLink stackOverflowLink = specificInfo.orElseGet(() -> {
                StackOverflowLink newLink = new StackOverflowLink();
                newLink.setId(link.getId());
                newLink.setUrl(link.getUrl());
                newLink.setLastUpdatedAt(link.getLastUpdatedAt());
                newLink.setTelegramChats(link.getTelegramChats());
                return newLink;
            });

            stackOverflowLink.setAnswerCount(response.answerCount());
            stackOverflowLink.setScore(response.score());

            if (specificInfo.isEmpty()) {
                stackOverflowLinkService.addLink(stackOverflowLink);
            } else {
                stackOverflowLinkService.updateLink(stackOverflowLink);
            }
        }

        return update.toString();
    }

    private String updateField(String fieldName, Long oldValue, Long newValue) {
        if (oldValue != null && !oldValue.equals(newValue)) {
            return String.format("%s изменилось: %d -> %d\n", fieldName, oldValue, newValue);
        }
        return "";
    }
}
