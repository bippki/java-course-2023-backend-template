package edu.java.service.jooq;

import edu.java.entity.GithubLink;
import edu.java.entity.Link;
import edu.java.repository.jooq.JooqGithubLinkRepository;
import edu.java.service.GithubLinkService;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@RequiredArgsConstructor
public class JooqGithubLinkService implements GithubLinkService {
    private final JooqGithubLinkRepository jooqGithubLinkRepository;
    @Override
    public Optional<GithubLink> getLink(Link link) {
        GithubLink gitHubLink = jooqGithubLinkRepository.getLink(link);
        return gitHubLink != null ? Optional.of(gitHubLink) : Optional.empty();
    }
    @Override
    public GithubLink addLink(GithubLink link) {
        return jooqGithubLinkRepository.addLink(link);
    }
    @Override
    public void updateLink(GithubLink link) {
        jooqGithubLinkRepository.updateLink(link);
    }
}
