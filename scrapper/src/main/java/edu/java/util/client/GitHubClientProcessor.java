package edu.java.util.client;

import edu.java.client.GithubWebClient;
import edu.java.entity.Link;
import java.net.URI;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class GitHubClientProcessor extends BaseClientProcessor {
    private static final Pattern GITHUB_PATH_PATTERN =
        Pattern.compile("^/(?<userName>[a-zA-Z0-9-]+)/(?<repositoryName>[\\w-.]+)(/)?$");
    private final GithubWebClient gitHubClient;

    public GitHubClientProcessor(GithubWebClient gitHubClient) {
        super("github.com");
        this.gitHubClient = gitHubClient;
    }

    @Override
    public boolean isCandidate(URI url) {
        return host.equalsIgnoreCase(url.getHost()) &&
            GITHUB_PATH_PATTERN.matcher(url.getPath()).matches();
    }

    @Override
    public Mono<String> getUpdate(Link link) {
        return gitHubClient.getUserRepository(link.getUrl().getPath())
            .map(response -> response.updatedAt().isAfter(link.getLastUpdatedAt()) ?
                "Репозиторий обновлён" : null);
    }
}
