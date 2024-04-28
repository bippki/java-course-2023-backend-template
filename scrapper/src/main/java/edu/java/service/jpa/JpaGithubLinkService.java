package edu.java.service.jpa;

import edu.java.entity.GithubLink;
import edu.java.entity.Link;
import edu.java.repository.jpa.JpaGitHubLinkRepository;
import edu.java.service.GithubLinkService;
import lombok.RequiredArgsConstructor;
import java.util.Optional;

@RequiredArgsConstructor
public class JpaGithubLinkService implements GithubLinkService {
    private final JpaGitHubLinkRepository jpaGitHubLinkRepository;

    @Override
    public Optional<GithubLink> getLink(Link link) {
        return jpaGitHubLinkRepository.findById(link.getId());
    }

    @Override
    public GithubLink addLink(GithubLink link) {
        return jpaGitHubLinkRepository.save(link);
    }

    @Override
    public void updateLink(GithubLink link) {
        jpaGitHubLinkRepository.save(link);
    }
}
