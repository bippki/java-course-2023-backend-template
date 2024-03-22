package edu.java.util.client;

import edu.java.client.StackOverflowWebClient;
import edu.java.entity.Link;
import edu.java.entity.dto.StackOverflowResponse;
import java.net.URI;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;



@Component
public class StackOverflowClientProcessor extends BaseClientProcessor {
    private static final Pattern STACK_OVERFLOW_PATH_PATTERN =
        Pattern.compile("^/questions/(?<questionId>\\d+)(/[\\w-]*)?(/)?$");
    private final StackOverflowWebClient stackOverflowClient;

    public StackOverflowClientProcessor(StackOverflowWebClient stackOverflowClient) {
        super("stackoverflow.com");
        this.stackOverflowClient = stackOverflowClient;
    }

    @Override
    public boolean isCandidate(URI url) {
        return host.equalsIgnoreCase(url.getHost()) &&
            STACK_OVERFLOW_PATH_PATTERN.matcher(url.getPath()).matches();
    }

    @Override
    public Mono<String> getUpdate(Link link) {
        Matcher matcher = STACK_OVERFLOW_PATH_PATTERN.matcher(link.getUrl().getPath());

        if (matcher.matches()) {
            return stackOverflowClient.getQuestionsInfo(matcher.group("questionId"))
                .map(response -> {
                    StackOverflowResponse.Question first = response.items().get(0);
                    return first.lastActivityDate().isAfter(link.getLastUpdatedAt()) ?
                        "Вопрос обновлён" : null;
                });
        }

        return Mono.empty();
    }
}
