package edu.java.util.client;

import edu.java.client.GithubWebClient;
import edu.java.entity.GithubLink;
import edu.java.entity.Link;
import edu.java.entity.dto.GithubResponse;
import edu.java.service.GithubLinkService;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class GithubClientProcessor extends BaseClientProcessor {
    private static final Pattern GITHUB_PATH_PATTERN =
        Pattern.compile("^/(?<userName>[a-zA-Z0-9-]+)/(?<repositoryName>[\\w-.]+)(/)?$");
    private final GithubWebClient gitHubClient;
    private final GithubLinkService githubLinkService;

    public GithubClientProcessor(GithubWebClient gitHubClient, GithubLinkService githubLinkService) {
        super("github.com");
        this.gitHubClient = gitHubClient;
        this.githubLinkService = githubLinkService;
    }

    @Override
    public boolean isCandidate(URI url) {
        return host.equals(url.getHost().toLowerCase())
            && GITHUB_PATH_PATTERN.matcher(url.getPath()).matches();
    }

    @Override
    public Mono<String> getUpdate(Link link) {
        Optional<GithubLink> specificInfo = githubLinkService.getLink(link);
        return gitHubClient.getUserRepository(link.getUrl().getPath())
            .map(response -> processUpdate(response, specificInfo, link));
    }

    private String processUpdate(GithubResponse response, Optional<GithubLink> specificInfo, Link link) {
        StringBuilder update = new StringBuilder();
        boolean isDirty = false;
        GithubLink gitHubLink = getEntity(specificInfo, link);

        if (response.updatedAt().isAfter(link.getLastUpdatedAt())) {
            update.append("Репозиторий обновлён\n");
            isDirty = true;
        }

        if (specificInfo.isEmpty() || !gitHubLink.getDefaultBranch().equals(response.defaultBranch())) {
            update.append(updateDefaultBranch(specificInfo, gitHubLink, response));
            isDirty = true;
        }

        if (specificInfo.isEmpty() || !gitHubLink.getForksCount().equals(response.forksCount())) {
            update.append(updateForksCount(specificInfo, gitHubLink, response));
            isDirty = true;
        }

        if (isDirty) {
            updateEntity(gitHubLink, specificInfo.isEmpty());
        }

        return update.toString();
    }

    private String updateDefaultBranch(Optional<GithubLink> specificInfo, GithubLink gitHubLink, GithubResponse response) {
        if (specificInfo.isPresent()) {
            return String.format("Основная ветка изменена на: %s -> %s\n",
                gitHubLink.getDefaultBranch(), response.defaultBranch());
        }
        gitHubLink.setDefaultBranch(response.defaultBranch());
        return "";
    }

    private String updateForksCount(Optional<GithubLink> specificInfo, GithubLink gitHubLink, GithubResponse response) {
        if (specificInfo.isPresent()) {
            return String.format("Количество форков изменилось: %d -> %d\n",
                gitHubLink.getForksCount(), response.forksCount());
        }
        gitHubLink.setForksCount(response.forksCount());
        return "";
    }

    private GithubLink getEntity(Optional<GithubLink> repositoryLink, Link link) {
        return repositoryLink.orElseGet(() -> {
            GithubLink gitHubLink = new GithubLink();
            gitHubLink.setId(link.getId());
            gitHubLink.setUrl(link.getUrl());
            gitHubLink.setLastUpdatedAt(link.getLastUpdatedAt());
            gitHubLink.setTelegramChats(link.getTelegramChats());
            return gitHubLink;
        });
    }

    private void updateEntity(GithubLink gitHubLink, boolean isNewEntity) {
        if (isNewEntity) {
            githubLinkService.addLink(gitHubLink);
        } else {
            githubLinkService.updateLink(gitHubLink);
        }
    }
}
